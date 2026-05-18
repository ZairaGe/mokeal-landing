export interface ICliente {
  id: number;
  nombre?: string | null;
  telefono?: string | null;
  email?: string | null;
  nifCif?: string | null;
  direccion?: string | null;
  municipio?: string | null;
  codigoPostal?: string | null;
  notas?: string | null;
  activo?: boolean | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
