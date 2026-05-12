import React, { useState, useEffect } from 'react'
import { Search, Plus } from 'lucide-react'

const API_BASE = 'http://localhost:8080'

export default function BooksCatalog() {
  const [books, setBooks] = useState([])
  const [searchQuery, setSearchQuery] = useState('')
  const [showAddModal, setShowAddModal] = useState(false)
  const [newBook, setNewBook] = useState({ title: '', author: '' })
  const [error, setError] = useState('')

  const fetchBooks = async () => {
    try {
      const res = await fetch(`${API_BASE}/books`)
      if (res.ok) setBooks(await res.json())
    } catch (err) {
      console.error("Error fetching books:", err)
    }
  }

  useEffect(() => {
    fetchBooks()
  }, [])

  const filteredBooks = books.filter(book => 
    book.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
    book.author.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const handleAddBook = async (e) => {
    e.preventDefault()
    setError('')
    if (newBook.title && newBook.author) {
      try {
        const res = await fetch(`${API_BASE}/books`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(newBook)
        })
        if (res.ok) {
          fetchBooks()
          setNewBook({ title: '', author: '' })
          setShowAddModal(false)
        } else {
          const errData = await res.json()
          setError(errData.message?.title || errData.message?.author || 'Validation Error')
        }
      } catch (err) {
        setError('Network error')
      }
    }
  }

  return (
    <div className="catalog-container">
      <header className="page-header flex-between">
        <div>
          <h1>Books Catalog</h1>
          <p>Manage and search library books</p>
        </div>
        <button className="btn btn-primary flex-center" onClick={() => setShowAddModal(true)}>
          <Plus size={18} style={{ marginRight: '8px' }} /> Add New Book
        </button>
      </header>

      <div className="search-bar">
        <Search className="search-icon" size={20} />
        <input 
          type="text" 
          placeholder="Search by title or author..." 
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Author</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {filteredBooks.map(book => (
              <tr key={book.bookId}>
                <td>#{book.bookId}</td>
                <td><strong>{book.title}</strong></td>
                <td>{book.author}</td>
                <td>
                  <span className={`badge ${book.available ? 'badge-success' : 'badge-danger'}`}>
                    {book.available ? 'Available' : 'Issued'}
                  </span>
                </td>
              </tr>
            ))}
            {filteredBooks.length === 0 && (
              <tr>
                <td colSpan="4" className="text-center empty-state">No books found matching your query.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showAddModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Add New Book</h2>
            {error && <div style={{color: 'red', marginBottom: '1rem', fontSize: '0.875rem'}}>{error}</div>}
            <form onSubmit={handleAddBook}>
              <div className="form-group">
                <label>Title</label>
                <input 
                  type="text" 
                  value={newBook.title} 
                  onChange={(e) => setNewBook({...newBook, title: e.target.value})} 
                  className={!newBook.title ? 'error-border' : ''}
                  required 
                />
              </div>
              <div className="form-group">
                <label>Author</label>
                <input 
                  type="text" 
                  value={newBook.author} 
                  onChange={(e) => setNewBook({...newBook, author: e.target.value})} 
                  className={!newBook.author ? 'error-border' : ''}
                  required 
                />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={() => {setShowAddModal(false); setError('');}}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Book</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
