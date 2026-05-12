import React from 'react'
import { Link, useLocation } from 'react-router-dom'
import { Home, BookOpen, Users, BookmarkPlus } from 'lucide-react'

export default function Sidebar() {
  const location = useLocation()

  const links = [
    { to: '/', label: 'Dashboard', icon: <Home size={20} /> },
    { to: '/books', label: 'Books Catalog', icon: <BookOpen size={20} /> },
    { to: '/members', label: 'Members Directory', icon: <Users size={20} /> },
    { to: '/issues', label: 'Issue & Return Desk', icon: <BookmarkPlus size={20} /> },
  ]

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h2>LibManager</h2>
      </div>
      <nav className="sidebar-nav">
        {links.map((link) => (
          <Link
            key={link.to}
            to={link.to}
            className={`nav-item ${location.pathname === link.to ? 'active' : ''}`}
          >
            {link.icon}
            <span>{link.label}</span>
          </Link>
        ))}
      </nav>
    </aside>
  )
}
