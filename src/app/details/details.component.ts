import { Component, OnInit, NgModule, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DiceapiService } from 'app/service/diceapi.service';
import { DatePipe } from '@angular/common';
import * as Chartist from 'chartist';
import chartist_tooltip from 'chartist-plugin-tooltip';
import chartist_pointlabels from 'chartist-plugin-pointlabels';
import chartist_axistitle from 'chartist-plugin-axistitle';
import { Subject } from 'rxjs';
@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit, OnDestroy {
  public id: String;
  public simulationDate: any;
  numDice: Number;
  numSides: Number;
  numRolls: Number;
  rollsCount: Map<Number, Number>;
  labels: string[];
  series: any[];
  distributionDataTable: any;
  distributionDtOptions: any;
  distributionData: [];
  public dtTrigger: Subject<any> = new Subject();
  @ViewChild('distributionTable', { static: true }) distributionTable;
  constructor(private _Activatedroute: ActivatedRoute,
    private diceapiService: DiceapiService,
    private datePipe: DatePipe) { }

  ngOnInit() {
    var that = this;
    this._Activatedroute.params.subscribe(params => { this.id = params['id']; });
    this.diceapiService.getAllSimulationById(this.id).subscribe(data => {
      console.log(data);
      this.simulationDate = this.datePipe.transform(data.simulationDate, 'MMM-dd-yy hh:mm:ss a');
      this.numDice = data.diceDistribution.dice;
      this.numSides = data.diceDistribution.sides;
      this.numRolls = data.diceDistribution.rolls;
      this.rollsCount = data.rollsCount;
      this.labels = Object.keys(this.rollsCount);
      this.series = Object["values"](this.rollsCount);
      this.loadDataTable();
      var datawebsiteViewsChart = {
        labels: this.labels,
        series: [
          this.series
        ]
      };
      var max = Math.max.apply(null, this.series);
      var optionswebsiteViewsChart = {
        axisX: {
          showGrid: true,
        },
        low: 0,
        high: max + 1,
        axisY: {
          onlyInteger: true,
          low: 0,
        },
        plugins: [
          chartist_pointlabels({
            textAnchor: 'middle'
          }),
          chartist_axistitle({
            axisX: {
              axisTitle: "Sum",
              axisClass: "ct-axis-title",
              offset: {
                x: 0,
                y: 0
              },
              textAnchor: "middle"
            },
            axisY: {
              axisTitle: "Rolls per Sum",
              axisClass: "ct-axis-title",
              offset: {
                x: 0,
                y: -1
              },
              flipTitle: false
            }
          })
        ]
      };
      var responsiveOptions: any[] = [
        [{
          seriesBarDistance: 0,
          axisX: {
            labelInterpolationFnc: function (value) {
              return value[0];
            }
          }
        }]
      ];
      var websiteViewsChart = new Chartist.Line('#websiteViewsChart', datawebsiteViewsChart, optionswebsiteViewsChart, responsiveOptions);
      var seq = 0;

      // Once the chart is fully created we reset the sequence
      websiteViewsChart.on('created', function () {
        seq = 0;
      });

      websiteViewsChart.on('draw', function (data) {
        let seq: any, delays: any, durations: any;
        seq = 0;
        delays = 80;
        durations = 500;
        if (data.type === 'line' || data.type === 'area') {
          data.element.animate({
            d: {
              begin: 600,
              dur: 700,
              from: data.path.clone().scale(1, 0).translate(0, data.chartRect.height()).stringify(),
              to: data.path.clone().stringify(),
              easing: Chartist.Svg.Easing.easeOutQuint
            }
          });
        } else if (data.type === 'point') {
          seq++;
          data.element.animate({
            opacity: {
              begin: seq * delays,
              dur: durations,
              from: 0,
              to: 1,
              easing: 'ease'
            }
          });
        }
      });

      seq = 0;
    });





  }

  loadDataTable() {
    var that = this;
    var dataMapped = [];
    for (let i = 0; i < that.labels.length; i++) {
      dataMapped.push({ "key": that.labels[i], "value": that.series[i] });
    }
    this.distributionDtOptions = {
      data: dataMapped,

      processing: true,
      columns: [
        { title: 'SUM', data: 'key' },
        {
          title: 'ROLLS PER SUM', data: 'value'
        }]
    };
    this.distributionDataTable = jQuery(this.distributionTable.nativeElement);
    this.distributionDataTable.DataTable(this.distributionDtOptions);
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }
}
