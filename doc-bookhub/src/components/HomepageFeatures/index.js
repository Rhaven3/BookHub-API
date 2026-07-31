import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

const FeatureList = [
  {
    title: 'Catalogue en ligne',
    Svg: require('@site/static/img/undraw_docusaurus_mountain.svg').default,
    description: (
      <>
        Consultez à tout moment les 2000 ouvrages de la bibliothèque, recherchez
        par titre, auteur ou ISBN, et filtrez par catégorie ou disponibilité.
      </>
    ),
  },
  {
    title: 'Emprunts & Réservations',
    Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        Empruntez un livre en quelques clics, suivez vos dates de retour et
        réservez les ouvrages indisponibles pour être notifié dès leur retour.
      </>
    ),
  },
  {
    title: 'Notation & Suivi',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Notez et commentez vos lectures, et suivez l&apos;activité de la
        bibliothèque grâce à des tableaux de bord dédiés aux lecteurs et bénévoles.
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}