import { IServicio } from 'app/entities/servicio/servicio.model';

export interface ITrabajador {
  id: number;
  nombre?: string | null;
  telefono?: string | null;
  email?: string | null;
  activo?: boolean | null;
  notas?: string | null;
  servicioses?: Pick<IServicio, 'id'>[] | null;
}

export type NewTrabajador = Omit<ITrabajador, 'id'> & { id: null };
