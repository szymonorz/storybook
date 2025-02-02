import { Outlet } from 'react-router'
import './App.css'
import PageHeader from './components/PageHeader'
import HomePage from './pages/HomePage'

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
