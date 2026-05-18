import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import TrabajadorResolve from './route/trabajador-routing-resolve.service';

const trabajadorRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/trabajador').then(m => m.Trabajador),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/trabajador-detail').then(m => m.TrabajadorDetail),
    resolve: {
      trabajador: TrabajadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/trabajador-update').then(m => m.TrabajadorUpdate),
    resolve: {
      trabajador: TrabajadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/trabajador-update').then(m => m.TrabajadorUpdate),
    resolve: {
      trabajador: TrabajadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trabajadorRoute;
