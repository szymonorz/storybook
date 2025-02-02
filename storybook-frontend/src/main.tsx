import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter, Routes, Route } from 'react-router'
import RegisterPage from './pages/RegisterPage.tsx'
import HomePage from './pages/HomePage.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route element={<App/>}>
        <Route index element={<HomePage/>}/>
        <Route path="register" element={<RegisterPage/>}/>
      </Route>
      
    </Routes>
  </BrowserRouter>,
)
