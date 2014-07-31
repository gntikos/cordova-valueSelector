var exec = require('cordova/exec');
var Plugin = function() {};


/* function call example:
 * 
 * valueselector.selectFromRange("Dialog Title", ["first", "second", "third"], 0,
 *     function(selection) {
 *         alert("You chose " + selection);
 *     },
 *	   function(msg) {
 *	      alert(msg);
 *	   }
 * ); 
 *
 */

/*
Plugin.prototype.selectFromList = function(args, onSuccess, onError) {
    return exec(onSuccess, onError, 'ValueSelector', 'openList', 
		[args.title || 'Select Value', args.options, args.selected || -1]
	);
}
*/

Plugin.prototype.fromList = function(title, options, init, onSuccess, onError) {
	return exec(onSuccess, onError, 'ValueSelector', 'openList', [title, options, init]);
}

/* function call example:
 * 
 * valueselector.selectFromRange("Dialog Title", 50, {"min": 10, "max": 100, "step": 10},  
 *     function(selection) {
 *         alert("You chose " + selection);
 *     },
 *	   function(msg) {
 *	      alert(msg);
 *	   }
 * ); 
 *
 */

/*
Plugin.prototype.selectFromRange = function(args, onSuccess, onError) {
	return exec(onSuccess, onError, 'ValueSelector', 'openRange', 
		[args.title || 'Select Value', args.init || 0, args.params || {'min':0, 'max':100, 'step':50}]
	);
}
*/

Plugin.prototype.fromRange = function(title, init, params, onSuccess, onError) {
	return exec(onSuccess, onError, 'ValueSelector', 'openRange', [title, init, params]);
}

module.exports = new Plugin();
