import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'mokealApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'userManagement.home.title' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'mokealApp.cliente.home.title' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'trabajador',
    data: { pageTitle: 'mokealApp.trabajador.home.title' },
    loadChildren: () => import('./trabajador/trabajador.routes'),
  },
  {
    path: 'tarifa',
    data: { pageTitle: 'mokealApp.tarifa.home.title' },
    loadChildren: () => import('./tarifa/tarifa.routes'),
  },
  {
    path: 'servicio',
    data: { pageTitle: 'mokealApp.servicio.home.title' },
    loadChildren: () => import('./servicio/servicio.routes'),
  },
  {
    path: 'factura',
    data: { pageTitle: 'mokealApp.factura.home.title' },
    loadChildren: () => import('./factura/factura.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
