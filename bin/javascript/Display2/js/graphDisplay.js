
//Global value for the selected circle
var selected;


$(document).ready(function() {
        //Collect all links from json.js
            var links = data;
            var nodes = {};


            // Compute automatically the distinct nodes with all their informations from the links.
            links.forEach(function(link) {
                

                link.source = nodes[link.source.name] || (nodes[link.source.name] = 
                    jQuery.each(link.source, function(i, val) {
                    i+': '+val                    
                    })
                );

                link.target = nodes[link.target.name] || (nodes[link.target.name] = 
                    jQuery.each(link.target, function(i, val) {
                    i+': '+val                    
                    })
                );
            });


            //Width of the window
            var width = $(window).width();
            var height = $(window).height();

            var y = $('#nav').height()
            var x = $('#nav').width()


            //Call to the function resize() on resize of the window
            d3.select(window).on('resize', resize);

            //Initialize force relation inside the graph
            var force = d3.layout.force()
                .nodes(d3.values(nodes))
                .links(links)
                .size([width, height])
                .linkDistance(25)
                .charge(-1000)
                .on('tick', tick)
                .start();


            //Create and initialize the svg for graph
            var svg = d3.select('#display').append('svg')
                .attr('width', width-x)
                .attr('height', height)
                .append('g'); 


            //Create and initialize all paths
            var path = svg.append('g').selectAll('path')
                .data(force.links())
                .enter().append('path')
                .attr('class', function(d) {
                    return 'link ' + d.type;
                })
                .attr('id', function(d) {
                    return d.source.id + '-' + d.target.id
                });
             

			var drag = force.drag()
			.on("dragstart", dragstart);
			
			function dragstart(d) {
				d3.select(".fixed").classed("fixed", force.drag().fixed = false);
				d3.select(this).classed("fixed", d.fixed = true);
			}
          
            //Create and initialize all nodes
            var circle = svg.append('g').selectAll('circle')
                .data(force.nodes())
                .enter().append('circle')
                .attr('r', 10)
                .attr('id', function(d) {
                    return d.id;
                })
				.call(drag);
				var thas = d3.select(this);
				


            //Create and initialize all text (Name of nodes)
            var text = svg.append('g').selectAll('text')
                .data(force.nodes())
                .enter().append('text')
                .attr('x', '2em')
                .attr('y', '-0.8em')
                .text(function(d) {
                    return d.name;
                });

            //function to resize svg, display and graph display
              function resize() {
                width = window.innerWidth, height = window.innerHeight;
                var x = $('nav').width()
                $('#display').attr('width',$(window).width()-x).attr('height',$(window).height());
                $('svg').attr('width', width-x).attr('height', height);
                force.size([width, height]).resume();
              }


            //function which placed all nodes.
            function tick() {
                path.attr('d', linkArc);
                circle.attr('transform', transform);
                text.attr('transform', transform);
            }

            //Function which draw arcs for the paths
            function linkArc(d) {
                var dx = d.target.x - d.source.x,
                    dy = d.target.y - d.source.y,
                    dr = Math.sqrt(dx * dx + dy * dy)*12;

                return 'M' + d.source.x + ',' + d.source.y + 'A' + dr + ',' + dr + ' 0 0,1 ' + d.target.x + ',' + d.target.y;
            }

            //Function which manage movement of nodes
            function transform(d) {
                return 'translate(' + d.x + ',' + d.y + ')';
            }

            //Click on one node for highlight it and its relations.
            $('circle').click(function() {
                selected = $(this);
				addClassSvg(selected,'selected');
                startRelation(links)
            });

			
			
			
			
            $('#txtNodeSearcher').bind("enterKey",function(e){
				search(nodes);
            });
            $('#txtNodeSearcher').keyup(function(e){
                if(e.keyCode == 13)
                {
                    $(this).trigger("enterKey");
                }
            });

           $('#btnNodeSearcher').click(function() {
				search(nodes);
            });
			
			


            //Number of relations display. Call to GetSource/Target
            $('#nbrelations').change(function() {
                var value = $(this).text();
                if (!(value == '' && value == null && value == '0')) {
                    startRelation(links)
                }
            });




            //Switch between Source and target selector. Call to GetSource/Target
            $('#buttonSourceTarget').click(function() {
                if ($(this).hasClass('source')) {
                    $(this).attr('class', 'target').html('Target');
                } else {
                    $(this).attr('class', 'source').html('Source');
                }
                startRelation(links);
            });
});

//Search function, highlight nodes corresponding to search input
function search(nodes){
	var string = $('#txtNodeSearcher').val()
	removeClassSvg($('.searched'), 'searched');
	if(!(string == null || string == '')){
		var str = string.toLowerCase();
		for(var i in nodes) {
			if (nodes[i].name.toLowerCase().indexOf(str) >= 0) {
				var cir = $('#' + nodes[i].id);
				addClassSvg(cir,'searched');
			}
		}
	}
}

//Function wich add a class of a SVG element
function addClassSvg(element,aClass){

    var classes = element.attr('class');
    if (classes == null || classes == '') {
        element.attr('class',aClass);
    }
    else{
        element.attr('class',classes + ' ' + aClass );
    }

}

//Function wich remove class to a list of SVG elements
function removeClassSvg(AllElement,aClass){
    AllElement.each(function(){
        element = $(this);
		addClassSvg($(element),'');
        classes = element.attr('class');
        var split = classes.split(' ');
        element.attr('class','');
        split.forEach(function(oneClass){
            if(!(oneClass == aClass)){
                addClassSvg(element,oneClass);
            }

        });
    });
}

// Unselect all links
function resetLinks(links) {
    links.forEach(function(link) {
        link.selected = false;
    });
}

//Select recursively all the source of a target.
function getSource(links,theCircle, value) {
    if (typeof related == 'undefined') {
        var related = [];
    }
    links.forEach(function(link) {
        setTimeout(function(){ 
        var source = link.source;
        var target = link.target;
        var pathSelected = source.id + '-' + target.id;
        var sourceselector = $('#' + source.id);

        if (!((link.selected) == true) && target.id == theCircle){

                link.selected = true;

                if (value > 1) {
                    var relate = getSource(links, source.id, value-1)
                    $.merge(related, relate)
                }

                $('#' + pathSelected).attr('class', 'link linksource');

                if (sourceselector.attr('class') != 'selected')
					addClassSvg(sourceselector,'source');

                related.push(link);
        }
        }, 0);
    });

    return related;

}

//Select recursively all the target of a source.
function getTarget(links,theCircle, value) {
    if (typeof related == 'undefined') {
        var related = [];
    }

    links.forEach(function(link) {
        setTimeout(function(){
        var source = link.source;
        var target = link.target;
        var pathSelected = source.id + '-' + target.id;
        var targetselector = $('#' + target.id);
        if (!(link.selected == true) && source.id == theCircle) {

            link.selected = true;

            if (value > 1) {
                var relate = getTarget(links, target.id, value-1);
                $.merge(related, relate);
            }
            $('#' + pathSelected).attr('class', 'link linktarget');
            if (targetselector.attr('class') != 'selected')
				addClassSvg(targetselector,'target');

            related.push(link);

               

        }
        }, 0);
    });

    return related;
}

//Prepare to load selections and call to getSource/Target
function startRelation(links) {

    var value = $('#nbrelations').val();
    var theCircle = selected.attr('id');

    $('.link').attr('class', 'link suit');

    removeClassSvg($('circle'),'selected');
    removeClassSvg($('circle'),'source');
    removeClassSvg($('circle'),'target');
	addClassSvg($('#'+theCircle),'selected');
	
    getSource(links, theCircle, value);
    getTarget(links, theCircle, value);
    resetLinks(links);
}