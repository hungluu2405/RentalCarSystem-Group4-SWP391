// // Call the dataTables jQuery plugin
// $(document).ready(function() {
//   $('#dataTable').DataTable();
// });
//
// $('#dataTable').DataTable( {
//   colReorder: true
// } );

// Call the dataTables jQuery plugin
$(document).ready(function() {
    $('#dataTable').DataTable({
        colReorder: true,
        language: {
            url: "https://cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json"
        }
    });
});
