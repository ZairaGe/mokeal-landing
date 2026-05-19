import dayjs from 'dayjs/esm';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { EstadoServicio } from 'app/entities/enumerations/estado-servicio.model';
import { Frecuencia } from 'app/entities/enumerations/frecuencia.model';
import { TipoServicio } from 'app/entities/enumerations/tipo-servicio.model';
import { ZonaTarifa } from 'app/entities/enumerations/zona-tarifa.model';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';
import { ITrabajador } from 'app/entities/trabajador/trabajador.model';

export interface IServicio {
  id: number;
  tipoServicio?: keyof typeof TipoServicio | null;
  zona?: keyof typeof ZonaTarifa | null;
  frecuencia?: keyof typeof Frecuencia | null;
  fecha?: dayjs.Dayjs | null;
  horaInicio?: string | null;
  duracionHoras?: number | null;
  latitud?: number | null;
  longitud?: number | null;
  numTrabajadores?: number | null;
  estado?: keyof typeof EstadoServicio | null;
  direccion?: string | null;
  municipio?: string | null;
  notas?: string | null;
  precioTotal?: number | null;
  descuento?: number | null;
  cliente?: Pick<ICliente, 'id'> | null;
  tarifa?: Pick<ITarifa, 'id'> | null;
  trabajadoreses?: Pick<ITrabajador, 'id'>[] | null;
}

export type NewServicio = Omit<IServicio, 'id'> & { id: null };
