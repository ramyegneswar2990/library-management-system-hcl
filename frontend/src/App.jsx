import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Sidebar from './components/Sidebar'
import Dashboard from './components/Dashboard'
import BooksCatalog from './components/BooksCatalog'
import MembersDirectory from './components/MembersDirectory'
import IssueDesk from './components/IssueDesk'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app-container">
        <Sidebar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/books" element={<BooksCatalog />} />
            <Route path="/members" element={<MembersDirectory />} />
            <Route path="/issues" element={<IssueDesk />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App
