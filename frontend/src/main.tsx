import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import App from './App'
import Home from './view/pages/Home'
import Store from './view/pages/Store'
import Admin from './view/pages/Admin'
import Search from './view/pages/Search'

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { index: true, element: <Home /> },
      { path: 'store', element: <Store /> },
      { path: 'admin', element: <Admin /> },
      { path: 'search', element: <Search /> },
    ],
  },
])

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)
