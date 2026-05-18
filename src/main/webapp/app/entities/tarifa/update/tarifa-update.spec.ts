import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TarifaService } from '../service/tarifa.service';
import { ITarifa } from '../tarifa.model';

import { TarifaFormService } from './tarifa-form.service';
import { TarifaUpdate } from './tarifa-update';

describe('Tarifa Management Update Component', () => {
  let comp: TarifaUpdate;
  let fixture: ComponentFixture<TarifaUpdate>;
  let activatedRoute: ActivatedRoute;
  let tarifaFormService: TarifaFormService;
  let tarifaService: TarifaService;

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

    fixture = TestBed.createComponent(TarifaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tarifaFormService = TestBed.inject(TarifaFormService);
    tarifaService = TestBed.inject(TarifaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tarifa: ITarifa = { id: 1399 };

      activatedRoute.data = of({ tarifa });
      comp.ngOnInit();

      expect(comp.tarifa).toEqual(tarifa);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITarifa>();
      const tarifa = { id: 15454 };
      vitest.spyOn(tarifaFormService, 'getTarifa').mockReturnValue(tarifa);
      vitest.spyOn(tarifaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarifa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(tarifa);
      saveSubject.complete();

      // THEN
      expect(tarifaFormService.getTarifa).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tarifaService.update).toHaveBeenCalledWith(expect.objectContaining(tarifa));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITarifa>();
      const tarifa = { id: 15454 };
      vitest.spyOn(tarifaFormService, 'getTarifa').mockReturnValue({ id: null });
      vitest.spyOn(tarifaService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarifa: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(tarifa);
      saveSubject.complete();

      // THEN
      expect(tarifaFormService.getTarifa).toHaveBeenCalled();
      expect(tarifaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITarifa>();
      const tarifa = { id: 15454 };
      vitest.spyOn(tarifaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarifa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tarifaService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
