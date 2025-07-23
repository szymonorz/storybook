import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter, Routes, Route } from 'react-router'
import RegisterPage from './components/auth/RegisterPage.tsx'
import HomePage from './components/home/HomePage.tsx'
import AuthorBooksPage from './components/books/AuthorBooksPage.tsx'
import CreateBookPage from './components/books/CreateBookPage.tsx'
import FavouritesPage from './pages/FavouritesPage.tsx'
import SettingsPage from './pages/SettingsPage.tsx'
import BookPage from './components/books/BookPage.tsx'
import CreateChapterPage from './components/chapter/CreateChapterPage.tsx'
import ChapterPage from './components/chapter/ChapterPage.tsx'
import SearchResultsPage from './components/search/SearchResultsPage.tsx'
import LoginPage from './components/auth/LoginPage.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route element={<App/>}>
        <Route index element={<HomePage/>}/>
        <Route path="home" element={<HomePage/>}/>
        <Route path="register" element={<RegisterPage/>}/>
        <Route path="books" element={<AuthorBooksPage/>}/>
        <Route path='book'>
          <Route path=':bookId' element={<BookPage/>}/>
          <Route path=':bookId/createChapter' element={<CreateChapterPage/>}/>
          <Route path=':bookId/chapter/:chapterNumber' element={<ChapterPage/>}/>
        </Route>
        <Route path="createBook" element={<CreateBookPage/>}/>
        <Route path="favourites" element={<FavouritesPage/>}/>
        <Route path="settings" element={<SettingsPage/>}/>
        <Route path="search" element={<SearchResultsPage/>}/>
        <Route path='login' element={<LoginPage/>}/>
      </Route>
      
    </Routes>
  </BrowserRouter>,
)
