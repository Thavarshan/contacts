import { readFileSync } from 'node:fs';

const messageFile = process.argv[2];
const message = readFileSync(messageFile, 'utf8').trim();
const firstLine = message.split(/\r?\n/, 1)[0] ?? '';
const pattern =
  /^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)(\([a-z0-9-]+\))?: .{1,72}$/;

if (!pattern.test(firstLine)) {
  console.error('Commit message must follow Conventional Commits.');
  console.error('Example: feat(web): add contact dashboard filter');
  process.exit(1);
}
