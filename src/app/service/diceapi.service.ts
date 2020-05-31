import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DiceDistribution } from 'app/model/DiceDistribution';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class DiceapiService {

  constructor(private http: HttpClient) {
  }

  getAllSimulations() {
    return this.http.get<any>(environment.apiUrl + "dice-api/getSimulations/");
  }

  getAllSimulationById(id : String) {
    return this.http.get<any>(environment.apiUrl + "dice-api/getSimulationById/?id=" + id);
  }

  simulate(dice : Number, rolls: Number, sides : Number ) {
    return this.http.get<DiceDistribution>(environment.apiUrl + "dice-api/simulate/?dice=" + dice + "&rolls=" + rolls + "&sides=" + sides);
  }

  getAllAggregates() {
    return this.http.get<any>(environment.apiUrl + "dice-api/getAggregates/");
  }

  getAggregateByDiceAndSides(dice: Number, sides: Number) {
    return this.http.get<any>(environment.apiUrl + "dice-api/getAggregatesByDiceAndSides/?dice=" + dice + "&sides=" + sides);
  }
}
