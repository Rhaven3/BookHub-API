
import { themes as prismThemes } from 'prism-react-renderer';



/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'BookHub',
  tagline: 'Plateforme de gestion de bibliothèque',
  favicon: 'img/favicon.ico',

  plugins: ['docusaurus-plugin-image-zoom'],

  future: {
    v4: true,
  },


  url: 'https://Rhaven3.github.io',
  baseUrl: '/BookHub-API/',

  organizationName: 'Rhaven3',     
  projectName: 'BookHub-API',        
  trailingSlash: false,

  onBrokenLinks: 'throw',
  deploymentBranch: 'docs',


  i18n: {
    defaultLocale: 'fr',
    locales: ['fr'],
  },

  presets: [
    [
      'classic',

      ({
        docs: {
          sidebarPath: './sidebars.js',

          editUrl:
            'https://github.com/Rhaven3/BookHub.git/tree/main/',
        },
        blog: {
          showReadingTime: true,
          feedOptions: {
            type: ['rss', 'atom'],
            xslt: true,
          },
          editUrl:
            'https://github.com/Rhaven3/BookHub.git/tree/main/',
          onInlineTags: 'warn',
          onInlineAuthors: 'warn',
          onUntruncatedBlogPosts: 'warn',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({

      image: 'img/docusaurus-social-card.jpg',

      zoom: {
        selector: '.markdown img',
        background: {
          light: 'rgb(255, 255, 255)',
          dark: 'rgb(50, 50, 50)',
        },

      },
      colorMode: {
        respectPrefersColorScheme: true,
      },
      navbar: {
        title: 'BookHub',
        logo: {
          alt: 'BookHub Logo',
          src: 'img/logo.svg',
        },
        items: [
          {
            type: 'doc',
            docId: 'cahierDesCharges',
            position: 'left',
            label: 'Cahier des charges',
          },

          {
            href: 'https://github.com/Rhaven3/BookHub.git',
            label: 'GitHub',
            position: 'right',
          },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Docs',
            items: [
              {
                label: 'Cahier des charges',
                to: '/docs/cahierDesCharges',
              },
            ],
          },
          {
            title: 'Projet',
            items: [
              {
                label: 'Association Quartier Solidaire',
                href: 'https://github.com/Rhaven3/BookHub.git',
              },
            ],
          },

        ],
        copyright: `Copyright © ${new Date().getFullYear()} BookHub - Quartier Solidaire. Built with Docusaurus.`,
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
      },
    }),
};

export default config;