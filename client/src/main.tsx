import {Root as AtUiRoot} from 'at-ui';
import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import {HelmetProvider} from 'react-helmet-async';
import {RouterProvider, createBrowserRouter} from 'react-router-dom';

import './globals.scss';
import './i18n';
import {ErrorPage, NotificacaoPreviaForm, SuccessPage} from './pages';
// import {NotificacaoPreviaFormTeste} from "./pages/notificacao-previa-form-teste";


const router = createBrowserRouter([
  {
    path: "/",
    element: <NotificacaoPreviaForm />,
    errorElement: <ErrorPage />,
    children: [
      {
          errorElement: <ErrorPage />,
          children: [
              {
                  path: "*",
                  element: <NotificacaoPreviaForm />,
              },
          ],
      },
  ],
  },
  {
    path: "/success",
    element: <SuccessPage />,
  },
]);


ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
      <HelmetProvider>
        <AtUiRoot>
          <RouterProvider router={router} />
        </AtUiRoot>
      </HelmetProvider>
    </React.StrictMode>
);
