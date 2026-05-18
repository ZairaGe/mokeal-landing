import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactura, NewFactura } from '../factura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactura for edit and NewFacturaFormGroupInput for create.
 */
type FacturaFormGroupInput = IFactura | PartialWithRequiredKeyOf<NewFactura>;

type FacturaFormDefaults = Pick<NewFactura, 'id'>;

type FacturaFormGroupContent = {
  id: FormControl<IFactura['id'] | NewFactura['id']>;
  numero: FormControl<IFactura['numero']>;
  fechaEmision: FormControl<IFactura['fechaEmision']>;
  fechaVencimiento: FormControl<IFactura['fechaVencimiento']>;
  baseImponible: FormControl<IFactura['baseImponible']>;
  iva: FormControl<IFactura['iva']>;
  total: FormControl<IFactura['total']>;
  estado: FormControl<IFactura['estado']>;
  notas: FormControl<IFactura['notas']>;
  servicio: FormControl<IFactura['servicio']>;
  cliente: FormControl<IFactura['cliente']>;
};

export type FacturaFormGroup = FormGroup<FacturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FacturaFormService {
  createFacturaFormGroup(factura?: FacturaFormGroupInput): FacturaFormGroup {
    const facturaRawValue = {
      ...this.getFormDefaults(),
      ...(factura ?? { id: null }),
    };
    return new FormGroup<FacturaFormGroupContent>({
      id: new FormControl(
        { value: facturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(facturaRawValue.numero, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      fechaEmision: new FormControl(facturaRawValue.fechaEmision, {
        validators: [Validators.required],
      }),
      fechaVencimiento: new FormControl(facturaRawValue.fechaVencimiento),
      baseImponible: new FormControl(facturaRawValue.baseImponible, {
        validators: [Validators.required],
      }),
      iva: new FormControl(facturaRawValue.iva, {
        validators: [Validators.required],
      }),
      total: new FormControl(facturaRawValue.total, {
        validators: [Validators.required],
      }),
      estado: new FormControl(facturaRawValue.estado, {
        validators: [Validators.required],
      }),
      notas: new FormControl(facturaRawValue.notas, {
        validators: [Validators.maxLength(500)],
      }),
      servicio: new FormControl(facturaRawValue.servicio, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(facturaRawValue.cliente, {
        validators: [Validators.required],
      }),
    });
  }

  getFactura(form: FacturaFormGroup): IFactura | NewFactura {
    return form.getRawValue() as IFactura | NewFactura;
  }

  resetForm(form: FacturaFormGroup, factura: FacturaFormGroupInput): void {
    const facturaRawValue = { ...this.getFormDefaults(), ...factura };
    form.reset({
      ...facturaRawValue,
      id: { value: facturaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FacturaFormDefaults {
    return {
      id: null,
    };
  }
}
