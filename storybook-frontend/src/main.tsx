import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { BrowserRouter, Routes, Route } from 'react-router'
import RegisterPage from './pages/RegisterPage.tsx'

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route index element={<App/>}/>
      <Route path="register" element={<RegisterPage/>}/>
    </Routes>
  </BrowserRouter>,
)
