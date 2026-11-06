const { test } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const html = fs.readFileSync(path.join(__dirname, '../src/main/resources/static/index.html'), 'utf8');
const script = html.split('<script>')[1].split('</script>')[0];
function setup(value, error = false) {
  const elements = new Map();
  const get = id => {
    if (!elements.has(id)) elements.set(id, { value: '', innerHTML: '', textContent: '', style: {}, addEventListener() {} });
    return elements.get(id);
  };
  get('limitInput').value = value;
  const urls = [];
  const context = { window: {}, document: { getElementById: get }, setTimeout,
    fetch: url => { urls.push(url); return error ? Promise.reject(new Error('<img src=x>')) : Promise.resolve({ json: () => Promise.resolve({ code: 200, data: [] }) }); }
  };
  vm.createContext(context); vm.runInContext(script, context);
  return { context, urls, get };
}
for (const [value, expected] of [['', 10], ['abc', 10], ['-5', 1], ['200', 100], ['25', 25]]) {
  test(`query limit ${JSON.stringify(value)} becomes ${expected}`, async () => {
    const { context, urls, get } = setup(value);
    context.loadConfessions(); await new Promise(setImmediate);
    assert.equal(urls[0], `/confession/recent?limit=${expected}`);
    assert.equal(get('limitInput').value, expected);
  });
}
test('network errors are displayed as fixed text', async () => {
  const { context, get } = setup('10', true);
  context.loadConfessions(); await new Promise(setImmediate);
  assert.ok(get('confessionContainer').textContent.length > 0);
  assert.doesNotMatch(get('confessionContainer').innerHTML, /img/);
});
test('existing user content escaping remains intact', () => {
  const { context } = setup('10');
  assert.equal(context.escapeHtml('<script>'), '&lt;script&gt;');
});
