import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter, Routes, Route } from 'react-router'
import RegisterPage from './components/auth/RegisterPage.tsx'
import HomePage from './components/home/HomePage.tsx'
import BooksPage from './components/books/BooksPage.tsx'
import CreateBookPage from './components/books/CreateBookPage.tsx'
import FavouritesPage from './pages/FavouritesPage.tsx'
import SettingsPage from './pages/SettingsPage.tsx'
import NotLoggedIn from './components/auth/NotLoggedIn.tsx'
import BookPage from './components/books/BookPage.tsx'
import CreateChapterPage from './components/chapter/CreateChapterPage.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route element={<App/>}>
        <Route index element={<HomePage/>}/>
        <Route path="home" element={<HomePage/>}/>
        <Route path="register" element={<RegisterPage/>}/>
        <Route path="books" element={<BooksPage/>}/>
        <Route path='book'>
          <Route path=':bookId' element={<BookPage/>}/>
          <Route path=':bookId/createChapter' element={<CreateChapterPage/>}/>
        </Route>
        <Route path="createBook" element={<CreateBookPage/>}/>
        <Route path="favourites" element={<FavouritesPage/>}/>
        <Route path="settings" element={<SettingsPage/>}/>

        <Route path='notLoggedIn' element={<NotLoggedIn/>}/>
      </Route>
      
    </Routes>
  </BrowserRouter>,
)
