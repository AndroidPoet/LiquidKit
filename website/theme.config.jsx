import { useRouter } from 'next/router'
import { useConfig } from 'nextra-theme-docs'

const ACCENT = '#2EC5CE'

const Logo = () => (
  <span
    style={{
      display: 'inline-flex',
      alignItems: 'center',
      gap: '0.55rem',
      fontWeight: 700,
      fontSize: '1.05rem',
    }}
  >
    <svg
      width="26"
      height="26"
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <defs>
        <linearGradient id="lk-logo-grad" x1="4" y1="2" x2="20" y2="22" gradientUnits="userSpaceOnUse">
          <stop stopColor="#4FA3FF" />
          <stop offset="1" stopColor="#2EC5CE" />
        </linearGradient>
      </defs>
      <path
        d="M12 2.5C12 2.5 5 10.2 5 15a7 7 0 0 0 14 0c0-4.8-7-12.5-7-12.5Z"
        fill="url(#lk-logo-grad)"
      />
      <path
        d="M9 13.5c0 2.2 1.3 3.8 3.2 4.2"
        stroke="#fff"
        strokeOpacity="0.85"
        strokeWidth="1.4"
        strokeLinecap="round"
      />
    </svg>
    LiquidKit
  </span>
)

export default {
  logo: <Logo />,
  project: {
    link: 'https://github.com/AndroidPoet/LiquidKit',
  },
  docsRepositoryBase: 'https://github.com/AndroidPoet/LiquidKit/tree/main/website',
  color: {
    hue: 200,
    saturation: 90,
  },
  sidebar: {
    defaultMenuCollapseLevel: 1,
  },
  toc: {
    backToTop: true,
  },
  navigation: {
    prev: true,
    next: true,
  },
  darkMode: true,
  footer: {
    content: (
      <span>
        Apache-2.0 © {new Date().getFullYear()}{' '}
        <a href="https://github.com/AndroidPoet" target="_blank" rel="noreferrer" style={{ color: ACCENT }}>
          AndroidPoet
        </a>
        . LiquidKit — Liquid Glass UI for Compose Multiplatform.
      </span>
    ),
  },
  head() {
    const { asPath, defaultLocale, locale } = useRouter()
    const { frontMatter } = useConfig()
    const base = 'https://androidpoet.github.io/LiquidKit'
    const url = base + (defaultLocale === locale ? asPath : `/${locale}${asPath}`)
    const title = frontMatter.title || 'LiquidKit'
    const description =
      frontMatter.description ||
      'Liquid Glass UI for Compose Multiplatform — Android shader rendering, genuine native iOS controls.'
    return (
      <>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="canonical" href={url} />
        <link rel="icon" href="/LiquidKit/favicon.svg" type="image/svg+xml" />
        <meta name="description" content={description} />
        <meta property="og:type" content="website" />
        <meta property="og:url" content={url} />
        <meta property="og:title" content={title} />
        <meta property="og:description" content={description} />
        <meta name="twitter:card" content="summary_large_image" />
        <meta name="twitter:title" content={title} />
        <meta name="twitter:description" content={description} />
      </>
    )
  },
  useNextSeoProps() {
    return { titleTemplate: '%s – LiquidKit' }
  },
}
