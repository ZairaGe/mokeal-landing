import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';

import { FacturaFormService } from './factura-form.service';
import { FacturaUpdate } from './factura-update';

describe('Factura Management Update Component', () => {
  let comp: FacturaUpdate;
  let fixture: ComponentFixture<FacturaUpdate>;
  let activatedRoute: ActivatedRoute;
  let facturaFormService: FacturaFormService;
  let facturaService: FacturaService;
  let servicioService: ServicioService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(FacturaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturaFormService = TestBed.inject(FacturaFormService);
    facturaService = TestBed.inject(FacturaService);
    servicioService = TestBed.inject(ServicioService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Servicio query and add missing value', () => {
      const factura: IFactura = { id: 8873 };
      const servicio: IServicio = { id: 24037 };
      factura.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Cliente query and add missing value', () => {
      const factura: IFactura = { id: 8873 };
      const cliente: ICliente = { id: 13484 };
      factura.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      vitest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      vitest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.clientesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factura: IFactura = { id: 8873 };
      const servicio: IServicio = { id: 24037 };
      factura.servicio = servicio;
      const cliente: ICliente = { id: 13484 };
      factura.cliente = cliente;

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(comp.serviciosSharedCollection()).toContainEqual(servicio);
      expect(comp.clientesSharedCollection()).toContainEqual(cliente);
      expect(comp.factura).toEqual(factura);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFactura>();
      const factura = { id: 30162 };
      vitest.spyOn(facturaFormService, 'getFactura').mockReturnValue(factura);
      vitest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(factura);
      saveSubject.complete();

      // THEN
      expect(facturaFormService.getFactura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturaService.update).toHaveBeenCalledWith(expect.objectContaining(factura));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFactura>();
      const factura = { id: 30162 };
      vitest.spyOn(facturaFormService, 'getFactura').mockReturnValue({ id: null });
      vitest.spyOn(facturaService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(factura);
      saveSubject.complete();

      // THEN
      expect(facturaFormService.getFactura).toHaveBeenCalled();
      expect(facturaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFactura>();
      const factura = { id: 30162 };
      vitest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturaService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCliente', () => {
      it('should forward to clienteService', () => {
        const entity = { id: 13484 };
        const entity2 = { id: 20795 };
        vitest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
