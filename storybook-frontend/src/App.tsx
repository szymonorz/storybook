import { Outlet } from 'react-router'
import './App.css'
import PageHeader from './components/PageHeader'

import './utils/i18n'

function App() {
  return (
    <>
        <div className='app'>
          <PageHeader/>
          <Outlet/>
        </div>
    </>
  )
}

export default App
