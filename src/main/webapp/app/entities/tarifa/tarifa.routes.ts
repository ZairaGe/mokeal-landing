import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TarifaResolve from './route/tarifa-routing-resolve.service';

const tarifaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tarifa').then(m => m.Tarifa),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tarifa-detail').then(m => m.TarifaDetail),
    resolve: {
      tarifa: TarifaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tarifa-update').then(m => m.TarifaUpdate),
    resolve: {
      tarifa: TarifaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tarifa-update').then(m => m.TarifaUpdate),
    resolve: {
      tarifa: TarifaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tarifaRoute;
