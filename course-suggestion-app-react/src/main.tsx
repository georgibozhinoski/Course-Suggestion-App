import {createRoot} from 'react-dom/client'
import './index.css'
import {RouterProvider} from "react-router";
import {router} from "./routes.tsx";
import {StrictMode} from "react";

import {ThemeProvider} from "./components/themeContext.tsx";

createRoot(document.getElementById('root')!).render(
    <ThemeProvider>
        <StrictMode>
            <RouterProvider router={router}/>
        </StrictMode>,
    </ThemeProvider>
)