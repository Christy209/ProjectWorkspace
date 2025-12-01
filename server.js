const express = require('express');
const fs = require('fs');
const path = require('path');
const simpleGit = require('simple-git');
const app = express();
const PORT = 4000;

app.use(express.json());

// Path to your config.properties file
const configFilePath = path.join(__dirname, '../ImportAndInwardBills/resources/config.properties');

// Git setup
const git = simpleGit({
    baseDir: path.join(__dirname, '../ImportAndInwardBills'),
    binary: 'git'
});

// GET /config -> read file content
app.get('/config', (req, res) => {
    fs.readFile(configFilePath, 'utf8', (err, data) => {
        if (err) return res.status(500).send('Error reading file: ' + err.message);
        res.type('text/plain');
        res.send(data);
    });
});

// POST /config -> update file content AND push to GitHub
app.post('/config', async (req, res) => {
    const { content, secret } = req.body;
    if (secret !== 'CHRISTY_EDIT') return res.status(403).send('Unauthorized');
    if (!content) return res.status(400).send('No content provided');

    try {
        // Write new content to file
        fs.writeFileSync(configFilePath, content, 'utf8');

        // Commit and push to GitHub
        await git.add('resources/config.properties');
        await git.commit(`Update config.properties via backend`);
        await git.push('origin', 'main');

        res.send('File updated and pushed to GitHub successfully!');
    } catch (err) {
        console.error(err);
        res.status(500).send('Error updating Git or file: ' + err.message);
    }
});

app.listen(PORT, () => {
    console.log(`Backend running at http://localhost:${PORT}`);
});
