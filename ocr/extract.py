import re
from collections import Counter

STOP = set("""и в не на с к по для от что это как он она они из у о""".split()) # как пример

def keywords(text, k=10):
    words = re.findall(r"[А-Яа-яA-Za-z]{4,}", text.lower())
    common = (w for w in words if w not in STOP)
    most = Counter(common).most_common(k)
    return [w for w, _ in most]