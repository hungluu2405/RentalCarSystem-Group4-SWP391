$(document).ready(function() {
    // Nếu đã init DataTable trước đó thì destroy
    if ( $.fn.DataTable.isDataTable('#dataTable') ) {
        $('#dataTable').DataTable().destroy();
    }

    // Init lại với colReorder và tiếng Việt
    $('#dataTable').DataTable({
        colReorder: true,
        language: {
            url: "https://cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json"
        },
        "columnDefs": [
            { "type": "num-fmt", "targets": 0 }
        ],
        "order": [[0, 'asc']]
    });
});
