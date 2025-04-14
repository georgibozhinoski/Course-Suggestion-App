# Frontend Development Guidelines



---

## Project Setup

```bash
npm install
npm run dev
```

Make sure to run this whenever dependencies are added or cloned from the repository.

---

## Project Structure

```plaintext
src/
├── api/            # Axios instance and API request logic
├── components/     # Reusable UI components (e.g., Button, Navbar)
├── pages/          # Route-level components (e.g., Login, Dashboard)
├── stores/         # Zustand stores (e.g., authStore)
├── routes.tsx      # Route definitions (public and protected)
├── App.tsx         # Main application component
└── main.tsx        # Entry point
```

---

## Routing

All routes are declared in `src/routes.tsx`. Nested routing is supported as needed.

### Public Routes

Accessible without authentication.  
**Example:**
```tsx
{
  path: "/login",
  element: <LoginPage />
}
```

### Protected Routes

Require authentication. Wrapped with `<ProtectedRoute />` to check if the user is logged in. If there is no logged in user, they automatically redirect to the login page

**Example:**
```tsx
{
    path: "/homepage",
        element: (
        <ProtectedRoute>
            <Homepage/>
        </ProtectedRoute>),
}
```
Note: Add protected routes as children of the "/" path , pages that are children of this path will have the navbar automatically present on top
```
 {
        path: "/",
        element: (
            <ProtectedRoute>
                <Navbar/>
            </ProtectedRoute>),
        children: [
            {
                path: "",
                element: <Homepage/>
            },
            // add here
```

Note: 404 handling is already implemented using a dedicated page — no need to worry about it.

---

## Styling

Tailwind CSS is used for all styles.

- Prefer using `shadcn/ui` components and customize them to match the Figma design
- **shadcn** components can be found [here](https://ui.shadcn.com/docs/components/accordion) and entire blocks [here](https://ui.shadcn.com/blocks)
- copy the code from the **shadcn** components, or install them using `npx shadcn@latest add component-name` in terminal, and edit them as needed


- Dark mode may be enforced to the app, depending on your system settings
- IF you need to change the tailwind classes, or add your own, edit `src/index.css` refer to [these docs](https://tailwindcss.com/docs/adding-custom-styles)
- Link to [tailwind docs](https://tailwindcss.com/docs/aspect-ratio) 

### Color System (Tailwind Customization)

Customize Tailwind's `tailwind.config.ts` to reflect your Figma mockup:

```ts
theme: {
  extend: {
    colors: {
      background: '#0E0E10',
      primary: '#1DB954',
      accent: '#BB86FC',
      // Add more from Figma as needed
    },
  },
},
darkMode: "class",
```

Apply classes like:

```tsx
<div className="bg-background text-primary p-4">
  Welcome to the Dashboard!
</div>
```

---

## Components

- Place reusable components in `src/components`
- Use `shadcn/ui` components as the base
- Follow the Figma design and override Tailwind classes where needed

---

## Authentication

- JWT token is stored in `localStorage` and `authStore` (Zustand)
- Use `authStore` for accessing and updating auth-related data
- Protect routes using a `<ProtectedRoute />` component that checks authentication in `src/routes.tsx`

---

## API Communication

- Use the centralized `axiosInstance.ts` located in `src/api/`
- Interceptors can handle auth headers and errors globally

**Example:**
```tsx
import axiosInstance from "@/api/axiosInstance";

const fetchUser = async () => {
  const res = await axiosInstance.get("/users/me");
  return res.data;
};
```

- The JWT token is applied to every request in the Authorization header, when a suer is logged in, via axios interceptors

Replace baseURL if backend is on different URL/port, in src/api/axiosInstance.ts
```ts
const axiosInstance = axios.create({
baseURL: 'http://localhost:9090/api/v1',
});
```

---

## State Management

- All global state is managed using Zustand
- Stores are placed in `src/stores`
- Currently, only `authStore` is implemented

**Example usage:**
```ts
// inside a React Component
const login = useAuthStore((s) => s.login);

...

await login(email, password);
```

---

## Forms

- Currently, forms use `useState` for managing values
- This may be change if more complex forms are added to the project

**Example:**
```tsx
const [email, setEmail] = useState("");
<input value={email} onChange={(e) => setEmail(e.target.value)} />
```

---

## Code Style & Formatting

- Follow standard TypeScript + React conventions
- Use `camelCase` for variables and functions, `PascalCase` for components
- Keep components clean and concise, extract logic into hooks where possible

---

## Component Best Practices

- Reuse before re-writing
- Split large components into subcomponents
- Use Tailwind for styling
- Avoid inline styles where possible, if custom styling is needed create a separate **.css** file with the same name as the component file 

---

## State/Effect Guidelines

- Always clean up side effects in `useEffect`
- Fetch data inside `useEffect` or with `react-query` if added later
- Prefer Zustand for shared state, use `useState` for local

---

