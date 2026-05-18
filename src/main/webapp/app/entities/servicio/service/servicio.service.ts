import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IServicio, NewServicio } from '../servicio.model';

export type PartialUpdateServicio = Partial<IServicio> & Pick<IServicio, 'id'>;

type RestOf<T extends IServicio | NewServicio> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestServicio = RestOf<IServicio>;

export type NewRestServicio = RestOf<NewServicio>;

export type PartialUpdateRestServicio = RestOf<PartialUpdateServicio>;

@Injectable()
export class ServiciosService {
  readonly serviciosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly serviciosResource = httpResource<RestServicio[]>(() => {
    const params = this.serviciosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of servicio that have been fetched. It is updated when the serviciosResource emits a new value.
   * In case of error while fetching the servicios, the signal is set to an empty array.
   */
  readonly servicios = computed(() =>
    (this.serviciosResource.hasValue() ? this.serviciosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/servicios');

  protected convertValueFromServer(restServicio: RestServicio): IServicio {
    return {
      ...restServicio,
      fecha: restServicio.fecha ? dayjs(restServicio.fecha) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class ServicioService extends ServiciosService {
  protected readonly http = inject(HttpClient);

  create(servicio: NewServicio): Observable<IServicio> {
    const copy = this.convertValueFromClient(servicio);
    return this.http.post<RestServicio>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(servicio: IServicio): Observable<IServicio> {
    const copy = this.convertValueFromClient(servicio);
    return this.http
      .put<RestServicio>(`${this.resourceUrl}/${encodeURIComponent(this.getServicioIdentifier(servicio))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(servicio: PartialUpdateServicio): Observable<IServicio> {
    const copy = this.convertValueFromClient(servicio);
    return this.http
      .patch<RestServicio>(`${this.resourceUrl}/${encodeURIComponent(this.getServicioIdentifier(servicio))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IServicio> {
    return this.http
      .get<RestServicio>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IServicio[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServicio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getServicioIdentifier(servicio: Pick<IServicio, 'id'>): number {
    return servicio.id;
  }

  compareServicio(o1: Pick<IServicio, 'id'> | null, o2: Pick<IServicio, 'id'> | null): boolean {
    return o1 && o2 ? this.getServicioIdentifier(o1) === this.getServicioIdentifier(o2) : o1 === o2;
  }

  addServicioToCollectionIfMissing<Type extends Pick<IServicio, 'id'>>(
    servicioCollection: Type[],
    ...serviciosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const servicios: Type[] = serviciosToCheck.filter(isPresent);
    if (servicios.length > 0) {
      const servicioCollectionIdentifiers = servicioCollection.map(servicioItem => this.getServicioIdentifier(servicioItem));
      const serviciosToAdd = servicios.filter(servicioItem => {
        const servicioIdentifier = this.getServicioIdentifier(servicioItem);
        if (servicioCollectionIdentifiers.includes(servicioIdentifier)) {
          return false;
        }
        servicioCollectionIdentifiers.push(servicioIdentifier);
        return true;
      });
      return [...serviciosToAdd, ...servicioCollection];
    }
    return servicioCollection;
  }

  protected convertValueFromClient<T extends IServicio | NewServicio | PartialUpdateServicio>(servicio: T): RestOf<T> {
    return {
      ...servicio,
      fecha: servicio.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestServicio): IServicio {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestServicio[]): IServicio[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
