import { TipoServicio } from 'app/entities/enumerations/tipo-servicio.model';
import { ZonaTarifa } from 'app/entities/enumerations/zona-tarifa.model';

export interface ITarifa {
  id: number;
  zona?: keyof typeof ZonaTarifa | null;
  tipoServicio?: keyof typeof TipoServicio | null;
  precioFirstHora?: number | null;
  precioHoraAdicional?: number | null;
  minimoHoras?: number | null;
  precioPorKm?: number | null;
  activa?: boolean | null;
}

export type NewTarifa = Omit<ITarifa, 'id'> & { id: null };
