import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { DiceapiService } from 'app/service/diceapi.service';
import { Subject } from 'rxjs';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { DiceDistribution } from 'app/model/DiceDistribution';
import { DatePipe, NgSwitchDefault } from '@angular/common';
import Swal from 'sweetalert2'
@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.css']
})
export class TableListComponent implements OnInit, OnDestroy {

  public simDataTable: any;
  aggregateDataTable: any;
  simDtOptions: any;
  aggregateDtOptions: any;
  simData: [];
  aggregateData: [];
  simModalData: any;
  simulateForm: FormGroup;
  submitted = false;
  diceDistribution: DiceDistribution;
  public dtTrigger: Subject<any> = new Subject();
  @ViewChild('simDataTable', { static: true }) simTable;
  @ViewChild('aggregateDataTable', { static: true }) aggregateTable;

  constructor(private diceapiService: DiceapiService,
    private router: Router, private formBuilder: FormBuilder,
    private datePipe : DatePipe) { }
 that = this;

 
  ngOnInit() {
   

    this.simulateForm = this.formBuilder.group({
      numDice: ['', [Validators.min(1), Validators.required]],
      numSides: ['', [Validators.min(1), Validators.required]],
      numRolls: ['', [Validators.min(1), Validators.required]]
    });
    var select = jQuery("#slctdice");
    var i = 0;
    for (i = 1; i <= 100; i++) {
      select.append(jQuery('<option></option>').val(i).html(i.toString()))
    }
    var that = this;
    jQuery.noConflict();
    this.getAllAggregates();
    this.getAllSimulationsFromSource();
    jQuery('#simTable').on('click', '.showAllData', function () {
      var tr = jQuery(this).closest('tr');
      var row = that.simDataTable.DataTable().row(tr);
      that.router.navigate(['/details/' + row.data().id]);
    });
    jQuery('#aggregateDataTable').on('click', '.showAggData', function () {
      var tr = jQuery(this).closest('tr');
      var row = that.aggregateDataTable.DataTable().row(tr);
      that.router.navigate(['/aggregate/' + row.data().dice+"/" + row.data().sides]);
    });

    

  }

  get f() { return this.simulateForm.controls; }

  onSubmit() {
    var that = this;
    this.submitted = true;
    if (this.simulateForm.invalid) {
      return;
    }
    Swal.fire({
      title: 'Simulate the following?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Simulate!',
      cancelButtonText: 'Cancel',
      html:
      'Dice :  ' + this.simulateForm.value.numDice +
      '<p>Sides : ' + this.simulateForm.value.numSides + '</p>'  +
      '<p>Rolls : ' + this.simulateForm.value.numRolls + '</p>' 
    }).then((result) => {
      if (result.value) {
        this.diceapiService.simulate(this.simulateForm.value.numDice, this.simulateForm.value.numRolls, this.simulateForm.value.numSides).subscribe(data => {
          this.diceDistribution = data;
          var tr = "";
          var html = ""
          var simMap = new Map();
          simMap = this.diceDistribution.simulation.rollsCount;
          console.log(simMap);

          if (data.simulationStatus == "SUCCESS") {
            console.log(simMap)
            Object.keys(simMap).forEach(function (key) {
              tr += '<tr><td>' + key + '</td><td>' + simMap[key] + '</td></tr>';
            });
            console.log(tr);
            html = '  <div class="row"> <div class="col-lg-12"><div class="card card-chart">' +
              '<div class="card-body "><div class="table-responsive " >' +
              '<table >' +
              '  <tr><th>SUM</th><th>ROLLS PER SUM</th></tr>' +
              ' </thead><tbody>' + tr +
              '</tbody>  </table>  </div></div></div></div></div>';
          }else{
            Object.keys(this.diceDistribution.validationMessages).forEach(function (key) {
              tr += '<tr><td>' + key + '</td><td>' + this.diceDistribution.validationMessages[key] + '</td></tr>';
            });
            html = '  <div class="row"> <div class="col-lg-12"><div class="card card-chart">' +
              '<div class="card-body swalTableDiv""><div class="table-responsive swalTableDiv"" id="swalTableDiv" >' +
              '<table>' +
              '  <tr><th>PROPERTY</th><th>ERROR MESSAGE</th></tr>' +
              ' </thead><tbody>' + tr +
              '</tbody>  </table>  </div></div></div></div></div>';
          }
      
          Swal.fire({
            title : 'Simulated!',
            icon : 'success',
            confirmButtonText: 'OK',
            html: html
          }
          )
         
          this.simDataTable.DataTable().destroy();
          this.aggregateDataTable.DataTable().destroy();
          this.getAllSimulationsFromSource();
          this.getAllAggregates();
        });
        console.log(this.diceDistribution);
      
      
      } 
    })
    
  }

  onReset() {
    this.submitted = false;
    this.simulateForm.reset();
  }

  getAllSimulationsFromSource() {
    var that = this;
    this.diceapiService.getAllSimulations().subscribe(data => {
      this.simData = data;
      this.simDtOptions = {
        data: this.simData,
        processing: true,
        columns: [
          { title: 'ID', data: 'id' },
          { title: 'Number of Dice', data: 'diceDistribution.dice' },
          { title: 'Number of Sides', data: 'diceDistribution.sides' },
          { title: 'Number of Rolls', data: 'diceDistribution.rolls' },
          { title: 'Simulation Date', data: 'simulationDate', render: function (data, type, row) {
            return that.datePipe.transform(data, 'MMM-dd-yy hh:mm:ss a');
        } },
          {
            title: 'Details', defaultContent: "<button class='showAllData btn btn-danger btn-round' > <span><i class='material-icons'>open_in_browser</i></span></button>", order: false
          }
        ]
      };
    }, err => { }, () => {

      this.simDataTable = jQuery(this.simTable.nativeElement);
      this.simDataTable.DataTable(this.simDtOptions);

    });
  }

  getAllAggregates(){
  var that = this;
  this.diceapiService.getAllAggregates().subscribe(data => {
    console.log(data);
    this.aggregateData = data;
    this.aggregateDtOptions = {
      data: this.aggregateData,
      processing: true,
      columns: [
        { title: 'Number of Dice', data: 'dice' },
        { title: 'Number of Sides', data: 'sides' },
        { title: 'Number of Rolls', data: 'rolls' },
        {
          title: 'Details', defaultContent: "<button class='showAggData btn btn-danger btn-round'><span><i class='material-icons'>open_in_browser</i></span></button>", order: false
        }
      ]
    };
  }, err => { }, () => {

    this.aggregateDataTable = jQuery(this.aggregateTable.nativeElement);
    this.aggregateDataTable.DataTable(this.aggregateDtOptions);

  });
}


  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

}
