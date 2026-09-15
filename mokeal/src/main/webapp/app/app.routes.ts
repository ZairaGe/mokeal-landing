import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./landing/landing.component'),
  },
  {
    path: 'nosotros',
    loadComponent: () => import('./about/about.component'),
  },
  {
    path: 'trabaja-con-nosotros',
    loadComponent: () => import('./trabaja-con-nosotros/trabaja-con-nosotros.component')
      .then(m => m.TrabajaConNosotrosComponent)
  },
  {
    path: '**',
    redirectTo: '',
  },
];

export default routes;