import React, { useState, useEffect } from 'react'
import { BookOpen, Users, BookMarked, BookmarkCheck } from 'lucide-react'
import { Link } from 'react-router-dom'

const API_BASE = 'http://localhost:8080'

export default function Dashboard() {
  const [stats, setStats] = useState({
    totalBooks: 0,
    availableBooks: 0,
    activeMembers: 0,
    totalIssued: 0
  })

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [booksRes, membersRes, issuesRes] = await Promise.all([
          fetch(`${API_BASE}/books`),
          fetch(`${API_BASE}/members`),
          fetch(`${API_BASE}/issues`)
        ])
        
        const books = await booksRes.json()
        const members = await membersRes.json()
        const issues = await issuesRes.json()

        setStats({
          totalBooks: books.length,
          availableBooks: books.filter(b => b.available).length,
          activeMembers: members.length,
          totalIssued: issues.filter(i => i.returnDate === null).length
        })
      } catch (err) {
        console.error("Error fetching stats:", err)
      }
    }
    fetchStats()
  }, [])

  return (
    <div className="dashboard-container">
      <header className="page-header">
        <h1>Dashboard</h1>
        <p>Overview of library metrics</p>
      </header>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon blue"><BookOpen size={24} /></div>
          <div className="stat-info">
            <h3>Total Books</h3>
            <p className="stat-value">{stats.totalBooks}</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon green"><BookmarkCheck size={24} /></div>
          <div className="stat-info">
            <h3>Available Books</h3>
            <p className="stat-value">{stats.availableBooks}</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon indigo"><Users size={24} /></div>
          <div className="stat-info">
            <h3>Active Members</h3>
            <p className="stat-value">{stats.activeMembers}</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon orange"><BookMarked size={24} /></div>
          <div className="stat-info">
            <h3>Total Issued</h3>
            <p className="stat-value">{stats.totalIssued}</p>
          </div>
        </div>
      </div>

      <div className="dashboard-actions">
        <h2>Quick Actions</h2>
        <div className="action-buttons">
          <Link to="/books" className="btn btn-primary">Add Book</Link>
          <Link to="/members" className="btn btn-secondary">Register Member</Link>
          <Link to="/issues" className="btn btn-primary">Issue Book</Link>
        </div>
      </div>
    </div>
  )
}
