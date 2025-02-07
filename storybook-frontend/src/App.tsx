import { Outlet } from 'react-router'
import './App.css'
import PageHeader from './components/PageHeader'
import HomePage from './pages/HomePage'
import { IntlProvider } from 'react-intl'

function App() {

  return (
    <>
      <IntlProvider locale='en'>
        <div className='app'>
          <PageHeader/>
          <Outlet/>
        </div>
      </IntlProvider>
    </>
  )
}

export default App
