import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DiceapiService } from 'app/service/diceapi.service';
import * as Chartist from 'chartist';
import { Subject } from 'rxjs';
@Component({
  selector: 'app-aggregate',
  templateUrl: './aggregate.component.html',
  styleUrls: ['./aggregate.component.css']
})
export class AggregateComponent implements OnInit, OnDestroy {
  public dice: Number;
  public sides : Number;
  rolls : Number;
  relativeDistribution : [];
  labels : string[];
   series : any[];
   distributionDataTable: any;
   distributionDtOptions: any;
   distributionData : [];
   public dtTrigger: Subject<any> = new Subject();
  constructor(private _Activatedroute: ActivatedRoute,
    private diceapiService: DiceapiService) { }
    @ViewChild('distributionTable', { static: true }) distributionTable;
  ngOnInit() {
   
    var that = this;
    this._Activatedroute.params.subscribe(params => { this.dice = params['dice'], this.sides = params['sides']; });
    this.diceapiService.getAggregateByDiceAndSides(this.dice, this.sides).subscribe(data => {
   
      that.rolls = data.rolls;
      that.relativeDistribution = data.relativeDistribution;
      this.labels = Object.keys(data.relativeDistribution);
      this.series = Object["values"](data.relativeDistribution);
      this.distributionData = data.relativeDistribution;
      this.loadDataTable();
     
     var chartData = {
      labels: this.labels,
        series: [
          this.series
        ]
      };
      
     var options = {
        labelInterpolationFnc: function(value) {
          return value[0]
        }
      };
      
     var responsiveOptions: any[]  = [
        ['screen and (min-width: 640px)', {
          chartPadding: 30,
          labelOffset: 100,
          labelDirection: 'explode',
          labelInterpolationFnc: function(value) {
            return value;
          }
        }],
        ['screen and (min-width: 1024px)', {
          labelOffset: 80,
          chartPadding: 20
        }]
      ];
      
    var aggregateViewsChart =new Chartist.Pie('#aggregateViewsChart', chartData, options, responsiveOptions)
     
    });
  }

  loadDataTable(){
    var that = this;
    let keys = Object.keys(this.distributionData);
    let values = Object['values'](this.distributionData);
    var dataMapped = [];
    for(let i=0; i<keys.length; i++){
      dataMapped.push({"key" : keys[i] , "value" : values[i]});
    }
    this.distributionDtOptions = {
      data: dataMapped,
     
      processing: true,
      columns: [
        { title: 'SUM', data: 'key' },
        { title: 'PERCENTAGE', data: 'value', render: function (data) {
          return data + " %";
        }}]
    };
    this.distributionDataTable = jQuery(this.distributionTable.nativeElement);
    this.distributionDataTable.DataTable(this.distributionDtOptions);
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
}
