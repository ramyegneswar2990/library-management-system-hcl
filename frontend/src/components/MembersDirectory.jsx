import React, { useState, useEffect } from 'react'
import { Plus, User } from 'lucide-react'

const API_BASE = 'http://localhost:8080'

export default function MembersDirectory() {
  const [members, setMembers] = useState([])
  const [showAddModal, setShowAddModal] = useState(false)
  const [selectedMember, setSelectedMember] = useState(null)
  const [issuedBooks, setIssuedBooks] = useState([])
  const [newMember, setNewMember] = useState({ name: '', email: '' })
  const [error, setError] = useState('')

  const fetchMembers = async () => {
    try {
      const res = await fetch(`${API_BASE}/members`)
      if (res.ok) setMembers(await res.json())
    } catch (err) {
      console.error(err)
    }
  }

  useEffect(() => {
    fetchMembers()
  }, [])

  const fetchIssuedBooks = async (memberId) => {
    try {
      const res = await fetch(`${API_BASE}/members/${memberId}/books`)
      if (res.ok) setIssuedBooks(await res.json())
    } catch (err) {
      console.error(err)
    }
  }

  const handleSelectMember = (member) => {
    setSelectedMember(member)
    fetchIssuedBooks(member.memberId)
  }

  const handleRegisterMember = async (e) => {
    e.preventDefault()
    setError('')
    if (newMember.name && newMember.email) {
      try {
        const res = await fetch(`${API_BASE}/members`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(newMember)
        })
        if (res.ok) {
          fetchMembers()
          setNewMember({ name: '', email: '' })
          setShowAddModal(false)
        } else {
          const errData = await res.json()
          setError(typeof errData.message === 'string' ? errData.message : 'Invalid input')
        }
      } catch (err) {
        setError('Network error')
      }
    }
  }

  return (
    <div className="members-container">
      <header className="page-header flex-between">
        <div>
          <h1>Members Directory</h1>
          <p>Manage library members</p>
        </div>
        <button className="btn btn-primary flex-center" onClick={() => setShowAddModal(true)}>
          <Plus size={18} style={{ marginRight: '8px' }} /> Register Member
        </button>
      </header>

      <div className="content-layout">
        <div className="table-container list-view">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
              </tr>
            </thead>
            <tbody>
              {members.map(member => (
                <tr 
                  key={member.memberId} 
                  onClick={() => handleSelectMember(member)}
                  className={selectedMember?.memberId === member.memberId ? 'selected-row' : 'clickable-row'}
                >
                  <td>#{member.memberId}</td>
                  <td><strong>{member.name}</strong></td>
                  <td>{member.email}</td>
                </tr>
              ))}
              {members.length === 0 && (
                <tr><td colSpan="3" className="text-center empty-state text-muted">No members yet.</td></tr>
              )}
            </tbody>
          </table>
        </div>

        {selectedMember && (
          <div className="details-pane card">
            <div className="profile-header">
              <div className="avatar"><User size={32} /></div>
              <div>
                <h2>{selectedMember.name}</h2>
                <p>{selectedMember.email}</p>
              </div>
            </div>
            
            <div className="issued-books-section">
              <h3>Currently Issued Books</h3>
              {issuedBooks.length > 0 ? (
                <ul className="issued-list">
                  {issuedBooks.map(issue => (
                    <li key={issue.issueId} className="issued-item">
                      <div className="book-info">
                        <strong>{issue.book.title}</strong>
                        <span className="text-sm text-muted">Issued: {issue.issueDate}</span>
                      </div>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-muted empty-state-small mt-4">No active issues for this member.</p>
              )}
            </div>
          </div>
        )}
      </div>

      {showAddModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Register Member</h2>
            {error && <div style={{color: 'red', marginBottom: '1rem', fontSize: '0.875rem'}}>{error}</div>}
            <form onSubmit={handleRegisterMember}>
              <div className="form-group">
                <label>Name</label>
                <input 
                  type="text" 
                  value={newMember.name} 
                  onChange={(e) => setNewMember({...newMember, name: e.target.value})} 
                  className={!newMember.name ? 'error-border' : ''}
                  required 
                />
              </div>
              <div className="form-group">
                <label>Email Address</label>
                <input 
                  type="email" 
                  value={newMember.email} 
                  onChange={(e) => setNewMember({...newMember, email: e.target.value})} 
                  className={!newMember.email ? 'error-border' : ''}
                  required 
                />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={() => {setShowAddModal(false); setError('');}}>Cancel</button>
                <button type="submit" className="btn btn-primary">Register</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
