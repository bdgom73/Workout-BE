$(document).ready(()=>{
  let cal = document.getElementById("calendar");
  let calendar = new FullCalendar.Calendar(cal,{
    timeZone: 'UTC',
    customButtons : customButtons,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'addSchedule dayGridMonth'
    },
    editable: true,
    droppable: true,
    dayMaxEvents: true, // when too many events in a day, show the popover
    events: 'https://fullcalendar.io/demo-events.json?overload-day',
    locale: 'ko',
    dateClick: function() {
      alert('a day has been clicked');
    },
    eventDrop: function(info) {
      alert(info.event.title + " was dropped on " + info.event.start.toISOString());
  
      if (!confirm("Are you sure about this change?")) {
        info.revert();
      }
    }
  });

  calendar.render();

  
  $("button.fc-prev-button").click(()=>{
    let date = calendar.getDate();
    let start = moment(date).format('YYYY-MM-DD');
    let end =  moment(date).add("41","d").format('YYYY-MM-DD');
    console.log(start,end,"zz");
  });

});

const customButtons = {
  addSchedule : {
    text : "일정추가",
    click : (e)=>{
      console.log(e);
    }
  }
}
