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
    path: '**',
    redirectTo: '',
  },
];

export default routes;