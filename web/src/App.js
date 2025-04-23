import React from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
  Navigate,
  useLocation,
} from 'react-router-dom';
import { FilePlus, Search, ScanText, Tags } from 'lucide-react';
import './App.css';

const NavItem = ({ to, icon: Icon, label }) => {
  const location = useLocation();
  const isActive = location.pathname === to;

  return (
    <li className={isActive ? 'nav-item active' : 'nav-item'}>
      <Link to={to}>
        <Icon size={20} />
        <span className="label">{label}</span>
      </Link>
    </li>
  );
};

const Placeholder = ({ title }) => (
  <div className="placeholder">
    <h2>{title}</h2>
    <p>Страница ещё в разработке.</p>
  </div>
);

function App() {
  return (
    <Router>
      <div className="layout">
        {/* Левый сайдбар */}
        <aside className="sidebar">
          <h1 className="brand">DMS</h1>
          <ul className="nav">
            <NavItem to="/upload" icon={FilePlus} label="Загрузка" />
            <NavItem to="/search" icon={Search} label="Поиск" />
            <NavItem to="/ocr" icon={ScanText} label="OCR" />
            <NavItem to="/metadata" icon={Tags} label="Метаданные" />
          </ul>
        </aside>

        {/* Основная область */}
        <main className="content">
          <Routes>
            <Route path="/" element={<Navigate to="/upload" replace />} />
            <Route path="/upload" element={<Placeholder title="Загрузка документов" />} />
            <Route path="/search" element={<Placeholder title="Поиск" />} />
            <Route path="/ocr" element={<Placeholder title="OCR" />} />
            <Route path="/metadata" element={<Placeholder title="Метаданные" />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
