# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is "오늘뭐입지?" (What to Wear Today), a Next.js 16 wardrobe management and outfit recommendation application. The app helps users organize their clothing items, create outfit combinations, and track their daily outfits via a calendar interface.

## Tech Stack

- **Framework**: Next.js 16.0.0 with App Router
- **React**: 19.2.0 (latest with React 19)
- **TypeScript**: Type-safe throughout (though `ignoreBuildErrors: true` is set in next.config)
- **Styling**: Tailwind CSS v4 with OKLCH color system
- **UI Components**: Extensive shadcn/ui component library with Radix UI primitives
- **Icons**: lucide-react
- **Forms**: react-hook-form with @hookform/resolvers and zod validation
- **Analytics**: Vercel Analytics integrated

## Commands

### Development
```bash
pnpm dev  # Start development server (Next.js dev mode)
```

### Build & Production
```bash
pnpm build  # Build for production
pnpm start  # Start production server
```

### Linting
```bash
pnpm lint  # Run ESLint
```

## Architecture & Key Patterns

### App Structure

The application follows Next.js App Router conventions:

- `app/` - App Router pages and layouts
  - `page.tsx` - Homepage with quick recommendation buttons and navigation
  - `layout.tsx` - Root layout with metadata, fonts (Geist, Geist_Mono), and Analytics
  - `wardrobe/` - Wardrobe management
    - `page.tsx` - Item list with filtering (client component)
    - `add/page.tsx` - Add new item form with image upload
    - `[id]/page.tsx` - Item detail, edit, and delete
  - `outfits/` - Outfit management
    - `page.tsx` - Outfit list with formality filtering (client component)
    - `create/page.tsx` - Create outfit by selecting items from wardrobe
    - `[id]/page.tsx` - Outfit detail, edit, and delete
  - `recommend/` - Recommendation system
    - `page.tsx` - Advanced filter page with customizable criteria
    - `result/page.tsx` - Recommendation result with outfit display
  - `calendar/page.tsx` - Outfit calendar tracking page (client component)
  - `globals.css` - Global styles with Tailwind v4 and custom CSS variables

- `components/` - React components
  - `ui/` - shadcn/ui components (50+ components from Radix UI)
  - `theme-provider.tsx` - Theme context provider

- `lib/` - Utilities
  - `utils.ts` - Single `cn()` helper for className merging (clsx + tailwind-merge)

### Path Aliases

The project uses `@/*` to reference the root directory:
- `@/components` → `/components`
- `@/lib/utils` → `/lib/utils`
- `@/app` → `/app`

### Component Patterns

1. **Client Components**: All interactive pages are marked `"use client"` and use React hooks extensively

2. **Shared Navigation Header**: Each page includes a consistent header with:
   - Back button (ArrowLeft icon) linking to parent page
   - Icon badge with page-specific color
   - Page title in serif font
   - Action buttons (Edit, Delete, Save, etc.) on the right

3. **Sample Data Pattern**: Currently using in-component sample data arrays (SAMPLE_ITEMS, SAMPLE_OUTFITS, MOCK_RECOMMENDATIONS) - these should eventually connect to a backend/database

4. **Formality Level System**: A 1-5 formality rating system is central to the app:
   - Custom OKLCH colors defined in globals.css (`--formality-1` through `--formality-5`)
   - Used in outfit categorization, filtering, and calendar visualization
   - Labels: Home (1), Neighborhood (2), Outing (3), Work (4), Formal (5)

5. **Form Pages**: Add/Edit pages follow a pattern:
   - Card-based layout with sections
   - Image upload with preview
   - Select dropdowns for categories
   - Validation before saving (e.g., required fields)
   - Cancel/Save buttons in header

6. **Dynamic Routes**: Detail pages use Next.js dynamic routes (`[id]`)
   - View/Edit mode toggle
   - AlertDialog for destructive actions (delete)
   - Mock data retrieval based on ID param

### Styling Approach

- **Tailwind v4** with OKLCH color space for sophisticated, perceptually uniform colors
- **Design System**: Minimal, sophisticated aesthetic with warm neutrals and refined sage accent colors
- **Typography**: Serif fonts (via `font-serif` class) for headlines, creating elegant contrast with UI sans-serif
- **Component Variants**: Using `class-variance-authority` (CVA) for component variants
- **CSS Variables**: Extensive use of CSS custom properties for theming
- **Dark Mode**: Complete dark mode support with separate OKLCH color definitions

### State Management

Currently using local React state (useState) in each page component. No global state management library is in use.

### Form Handling

The project includes react-hook-form and zod for form validation, though forms aren't fully implemented yet in the main pages.

## Development Notes

### TypeScript Configuration

- `ignoreBuildErrors: true` is set in next.config.mjs - type errors won't block builds
- Strict mode is enabled in tsconfig.json
- Module resolution uses bundler mode

### Image Handling

- `unoptimized: true` is set for Next.js images in next.config.mjs
- Static images are expected in the public directory

### shadcn/ui Configuration

The project uses the "new-york" style variant with:
- RSC (React Server Components) enabled
- CSS variables for theming
- Neutral base color
- No prefix for Tailwind classes
- Lucide for icons

### Current Limitations

1. All data is currently mocked/hardcoded in components
2. No backend API integration
3. No authentication or user management
4. No real image upload functionality
5. TypeScript errors are suppressed at build time

## User Flows & Navigation

### Quick Recommendation Flow (Homepage → Result)
1. User clicks "업무 복장" (Work Outfit) or "캐주얼 외출" (Casual Outing)
2. Redirects to `/recommend/result` with preset query params:
   - Work: `minRating=4&minFormality=4&excludeRecent=true`
   - Casual: `minRating=3&minFormality=3&excludeRecent=true`
3. Result page shows recommended outfit with option to "Recommend Again" or "Select This Outfit"

### Advanced Recommendation Flow
1. User clicks "Advanced Filter" button on homepage
2. Navigate to `/recommend` page
3. Adjust sliders for minimum rating and formality
4. Optionally select a required item to include
5. Toggle exclude recent outfits (2 days)
6. Click "Get Recommendation" → navigate to `/recommend/result` with custom params

### Wardrobe Management Flow
- `/wardrobe` → List all items with filtering
- Click "Add Item" → `/wardrobe/add` → Save → back to `/wardrobe`
- Click item card → `/wardrobe/[id]` → View/Edit/Delete

### Outfit Management Flow
- `/outfits` → List all outfits with formality filtering
- Click "Create Outfit" → `/outfits/create` → Select items → Set rating/formality → Save
- Click outfit card → `/outfits/[id]` → View/Edit/Delete

## Adding New Features

When adding features to this codebase:

1. **New Pages**: Create in `app/[route]/page.tsx` following App Router conventions
2. **New UI Components**: Use shadcn/ui CLI or manually add to `components/ui/`
3. **Styling**: Use the established OKLCH color system and maintain the minimal aesthetic
4. **Client Interactivity**: Mark components `"use client"` when using hooks or browser APIs
5. **Path Imports**: Always use `@/` path aliases for imports
6. **Formality Colors**: Use the existing formality color scale (1-5) when dealing with outfit formality levels
7. **Navigation**: Use Next.js `<Link>` component with `asChild` prop for button navigation
8. **Icons**: Import from `lucide-react` and maintain consistent sizing (typically w-4 h-4 or w-8 h-8)
