from fastapi import FastAPI, Request, HTTPException, Response
import httpx

app = FastAPI(title="API Gateway")

SERVICE_MAP = {
    "search": "http://search:8080/api/search",
    "files":  "http://storage:8080/api/files",
}

@app.api_route(
    "/api/{service}", 
    methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"]
)
@app.api_route(
    "/api/{service}/{path:path}", 
    methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"]
)
async def proxy(service: str, request: Request, path: str = ""):
    if service not in SERVICE_MAP:
        raise HTTPException(status_code=404, detail="Service not found")

    target_base = SERVICE_MAP[service].rstrip("/")
    url = f"{target_base}/{path}" if path else target_base

    headers = {
        k: v for k, v in request.headers.items()
        if k.lower() != "host"
    }

    async with httpx.AsyncClient() as client:
        resp = await client.request(
            method=request.method,
            url=url,
            headers=headers,
            params=request.query_params,
            content=await request.body()
        )

    excluded_headers = {"content-encoding", "transfer-encoding", "content-length"}
    response_headers = {
        k: v for k, v in resp.headers.items()
        if k.lower() not in excluded_headers
    }

    return Response(
        content=resp.content,
        status_code=resp.status_code,
        headers=response_headers,
        media_type=resp.headers.get("content-type")
    )