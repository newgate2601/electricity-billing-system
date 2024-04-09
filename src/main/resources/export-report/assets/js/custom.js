$(document).ready(function(){
    var table = $('#example').DataTable({
        dom: 'Bfrtip',
        ajax: {
            url: 'http://localhost:8080/api/v1/bill/list',
            dataSrc: 'content'
        },
        columns: [
            { data: 'apartment.customerEntity.name' },
            { 
                data: 'apartment.customerEntity.birthday',
                render: function(data) {
                    var date = new Date(data * 1000);
                    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                }
            },
            { data: 'apartment.addressEntity.streetName' },
            { data: 'apartment.addressEntity.city' },
            { data: 'billCode' },
            { data: 'price' }
        ],
        buttons: ['copy', 'csv', 'excel', 'pdf', 'print']
    });

    table.buttons().container()
    .appendTo('#example_wrapper .col-md-6:eq(0)');
});