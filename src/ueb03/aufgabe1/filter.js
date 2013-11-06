/**  ####################################
 *          N O D E J S
 *    call: $:node filter.js "hallo welt hallo hallo welt welt Welt demo Welt"
 *   ####################################
 * Created with JetBrains WebStorm.
 * User: Julian
 * Date: 06.11.13
 * Time: 12:52
 */
var histo = {};
process.argv[2].split(" ").forEach(function(val){
    if (val in histo) histo[val]++;
    else histo[val] = 1;
});
Object.keys(histo).sort(function(a,b) {
    return a.toLocaleLowerCase().localeCompare(b.toLocaleLowerCase());
}).forEach(function(val){
    console.log(val + " : " + histo[val]);
});
