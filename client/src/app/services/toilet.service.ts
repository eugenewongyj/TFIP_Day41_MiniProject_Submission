import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Toilet } from '../models/toilet';
import { lastValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ToiletService {

  private GET_ALL_TOILETS_API: string = "/api/v1/toilets/all";

  private GET_TOILET_BY_NAME_API: string = "/api/v1/toilets";

  constructor(private httpClient: HttpClient) { }

  getAllToilets(): Promise<Toilet[]> {
    return lastValueFrom(this.httpClient
      .get<Toilet[]>(this.GET_ALL_TOILETS_API));
  }

  getToiletByName(name: string) {
    return this.httpClient.get<Toilet>(this.GET_TOILET_BY_NAME_API + '/' + name);
  }


}
