import { Outlet } from 'react-router'
import './App.css'
import PageHeader from './components/shared/PageHeader'

import './utils/i18n'
import AuthProvider from './components/auth/AuthProvider'

function App() {
  return (
    <AuthProvider>
        <div className='app'>
          <PageHeader/>
          <Outlet/>
        </div>
    </AuthProvider>
  )
}

export default App
