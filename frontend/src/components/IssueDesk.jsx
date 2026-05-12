import React, { useState, useEffect } from 'react'
import { BookmarkPlus, Undo2 } from 'lucide-react'

const API_BASE = 'http://localhost:8080'

export default function IssueDesk() {
  const [activeIssues, setActiveIssues] = useState([])
  const [issueForm, setIssueForm] = useState({ memberId: '', bookId: '' })
  const [error, setError] = useState('')

  const fetchIssues = async () => {
    try {
      const res = await fetch(`${API_BASE}/issues`)
      if (res.ok) {
        const data = await res.json()
        setActiveIssues(data.filter(issue => issue.returnDate === null))
      }
    } catch (err) {
      console.error(err)
    }
  }

  useEffect(() => {
    fetchIssues()
  }, [])

  const handleIssueBook = async (e) => {
    e.preventDefault()
    setError('')
    if (issueForm.memberId && issueForm.bookId) {
      try {
        const res = await fetch(`${API_BASE}/issues/issue`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ 
            memberId: parseInt(issueForm.memberId), 
            bookId: parseInt(issueForm.bookId) 
          })
        })
        if (res.ok) {
          fetchIssues()
          setIssueForm({ memberId: '', bookId: '' })
        } else {
          const errData = await res.json()
          setError(errData.message || 'Error issuing book')
        }
      } catch (err) {
        setError('Network error')
      }
    }
  }

  const handleReturnBook = async (issueId) => {
    try {
      const res = await fetch(`${API_BASE}/issues/return/${issueId}`, {
        method: 'PUT'
      })
      if (res.ok) {
        fetchIssues()
      } else {
        const errData = await res.json()
        alert(errData.message || 'Error returning book')
      }
    } catch (err) {
      alert('Network error')
    }
  }

  return (
    <div className="issue-desk-container">
      <header className="page-header">
        <h1>Issue & Return Desk</h1>
        <p>Manage active library transactions</p>
      </header>

      <div className="desk-layout">
        <div className="card issue-form-card">
          <div className="card-header">
            <BookmarkPlus size={20} className="icon-blue" />
            <h2>Issue a Book</h2>
          </div>
          {error && <div style={{color: 'red', marginBottom: '1rem', fontSize: '0.875rem'}}>{error}</div>}
          <form onSubmit={handleIssueBook} className="issue-form">
            <div className="form-group">
              <label>Member ID</label>
              <input 
                type="number" 
                placeholder="Enter Member ID"
                value={issueForm.memberId} 
                onChange={(e) => setIssueForm({...issueForm, memberId: e.target.value})} 
                required 
              />
            </div>
            <div className="form-group">
              <label>Book ID (Available)</label>
              <input 
                type="number" 
                placeholder="Enter Book ID"
                value={issueForm.bookId} 
                onChange={(e) => setIssueForm({...issueForm, bookId: e.target.value})} 
                required 
              />
            </div>
            <button type="submit" className="btn btn-primary full-width mt-4">Issue Book</button>
          </form>
        </div>

        <div className="active-issues-section">
          <h2>Currently Active Issues</h2>
          <div className="table-container mt-4">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Issue ID</th>
                  <th>Member Name</th>
                  <th>Book Title</th>
                  <th>Issue Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {activeIssues.map(issue => (
                  <tr key={issue.issueId}>
                    <td>#{issue.issueId}</td>
                    <td>{issue.member.name}</td>
                    <td><strong>{issue.book.title}</strong></td>
                    <td>{issue.issueDate}</td>
                    <td>
                      <button 
                        className="btn btn-sm btn-outline-danger flex-center"
                        onClick={() => handleReturnBook(issue.issueId)}
                      >
                        <Undo2 size={14} style={{ marginRight: '4px' }} /> Return Book
                      </button>
                    </td>
                  </tr>
                ))}
                {activeIssues.length === 0 && (
                  <tr>
                    <td colSpan="5" className="text-center empty-state">No active issues at the moment.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}
