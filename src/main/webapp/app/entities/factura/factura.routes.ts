import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import FacturaResolve from './route/factura-routing-resolve.service';

const facturaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/factura').then(m => m.Factura),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/factura-detail').then(m => m.FacturaDetail),
    resolve: {
      factura: FacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/factura-update').then(m => m.FacturaUpdate),
    resolve: {
      factura: FacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/factura-update').then(m => m.FacturaUpdate),
    resolve: {
      factura: FacturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default facturaRoute;
