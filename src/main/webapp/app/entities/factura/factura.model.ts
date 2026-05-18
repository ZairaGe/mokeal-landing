import dayjs from 'dayjs/esm';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { EstadoFactura } from 'app/entities/enumerations/estado-factura.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IFactura {
  id: number;
  numero?: string | null;
  fechaEmision?: dayjs.Dayjs | null;
  fechaVencimiento?: dayjs.Dayjs | null;
  baseImponible?: number | null;
  iva?: number | null;
  total?: number | null;
  estado?: keyof typeof EstadoFactura | null;
  notas?: string | null;
  servicio?: Pick<IServicio, 'id'> | null;
  cliente?: Pick<ICliente, 'id'> | null;
}

export type NewFactura = Omit<IFactura, 'id'> & { id: null };
