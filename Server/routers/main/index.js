const leftPad = (value) => { 
    if (value >= 10) { 
        return value; 
    } 
    return `0${value}`; 
};

const toStringByFormatting = (source, delimiter = '-') => { 
    const year = source.getFullYear();
    const month = leftPad(source.getMonth() + 1);
    const day = leftPad(source.getDate());
    return [year, month, day].join(delimiter);
};

module.exports = (pool) => {
    const express = require('express');
    const multer = require('multer');
    const storage = multer.diskStorage({
        destination: function (req, file, cb) {
            cb(null, 'uploads/');
        },
        filename: function (req, file, cb) {
            cb(null, `${Date.now()}-${req.body.user_id}-${file.originalname}`);
        }
    });
    const upload = multer({ storage: storage })
    const router = express.Router();

    router.get('/items', async (req, res) => {
        const {query: {category_id, address, key}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result1] = await conn.query('SELECT I.It_id, I.U_id, (SELECT COUNT(*) FROM BID B'
                                                                +' WHERE B.It_id = I.It_id) Bid_count'
                                            + ' FROM ITEM I'
                                            + ' WHERE Expire_date < NOW()'
                                            + ` AND Is_end = '0'`);
            for (item of result1) {
                if (!item.Bid_count) {
                    await conn.query(`UPDATE ITEM SET Is_end = '2'`
                                    + ' WHERE It_id = ?', [item.It_id]);
                } else {
                    await conn.query(`UPDATE ITEM SET Is_end = '1'`
                                    + ' WHERE It_id = ?', [item.It_id]);
                    await conn.query('INSERT INTO ROOM(It_id, Buy_id, Sell_id)'
                                    + ' VALUES(?, (SELECT U_id FROM BID'
                                                + ' WHERE It_id = ?'
                                                + ' ORDER BY Create_date DESC LIMIT 1), ?)',
                                                [item.It_id, item.It_id, item.U_id]);
                }
            }

            let sql = 'SELECT It_id, Name, Quick_price,'
                        + ' Current_price, Create_date, Img'
                        + ' FROM ITEM'
                        + ' WHERE Ad_id = (SELECT Ad_id FROM ADDRESS'
                                        + ' WHERE Name = ?)'
                        + ' AND Expire_date > NOW()'
                        + ` AND Is_end = '0'`;

            if (parseInt(category_id)) {
                sql += ' AND c_id = ?';
            }
            if (key) {
                sql += ` AND LOWER(Name) LIKE '%${key.toLowerCase()}%'`
            }
            sql += ' ORDER BY Create_date DESC'

            let result2 = null;
            if (!parseInt(category_id)) {
                [result2] = await conn.query(sql, [address]);
            } else {
                [result2] = await conn.query(sql, [address, category_id]);
            }

            const item_list = result2.map((item_info) => {
                return {
                    item_id: item_info.It_id,
                    item_name: item_info.Name,
                    current_price: item_info.Current_price,
                    immediate_price: item_info.Quick_price,
                    created_date: item_info.Create_date.toLocaleDateString(),
                    img_url: item_info.Img,
                    address
                };
            });
            res.status(200).json({
                item_list
            });
        } catch (err) {
            console.log(err);
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.get('/item', async (req, res) => {
        const {query: {item_id}} = req;
        let conn = null;

        try { 
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT M.U_id, M.Name User_name, A.Name Address,'
                                            + ' (SELECT ROUND(AVG(Score), 1) FROM RATING'
                                            + ' WHERE S_id = I.U_id) Score,'
                                            + ' I.It_id, I.Quick_price, I.Current_price,'
                                            + ' I.Create_date, I.Expire_date,'
                                            + ' I.Description, I.Name Item_name, I.Img'
                                            + ' FROM ITEM I, MEMBER M, ADDRESS A'
                                            + ' WHERE I.U_id = M.U_id'
                                            + ' AND I.Ad_id = A.Ad_id'
                                            + ' AND I.It_id = ?', [item_id]);
            res.status(200).json({
                seller_id: result[0].U_id,
                seller_name: result[0].User_name,
                seller_rate: !result[0].Score ? 0 : result[0].Score,
                address: result[0].Address,
                item_id: result[0].It_id,
                item_name: result[0].Item_name,
                immediate_price: result[0].Quick_price,
                current_price: result[0].Current_price,
                created_date: toStringByFormatting(result[0].Create_date),
                expired_date: toStringByFormatting(result[0].Expire_date),
                description: result[0].Description,
                img_url: result[0].Img
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.post('/bid', async (req, res) => {
        const {body: {user_id, price, item_id}} = req;
        let conn = null;
        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT Min_bid_unit, Current_price,'
                                            + ' Is_end, Quick_price, U_id'
                                            + ' FROM ITEM'
                                            + ' WHERE It_id = ?', [item_id]);
            const {
                Min_bid_unit: min_bid_unit,
                Current_price: current_price,
                Is_end: is_end,
                Quick_price: quick_price,
                U_id: seller_id
            } = result[0];

            if (is_end !== '0') {
                res.status(400).json({
                    message: '유효하지 않은 상품입니다'
                });
            } else if (user_id === seller_id) {
                res.status(400).json({
                    message: '자신의 상품은 입찰할 수 없습니다'
                });
            } else if (price <= current_price) {
                res.status(400).json({
                    message: '입찰가는 현재가보다 커야합니다'
                });
            } else if (price >= quick_price) {
                res.status(400).json({
                    message: '즉시구매 해주세요'
                });
            } else if ((price - current_price) % min_bid_unit !== 0) {
                res.status(400).json({
                    message: '최소입찰단위를 맞춰주세요'
                });
            } else {
                await conn.query('INSERT INTO BID(Price, U_id, It_id)'
                                + ' VALUES(?, ?, ?)', [price, user_id, item_id]);
                await conn.query('UPDATE ITEM'
                                + ' SET Current_price = ?'
                                + ' WHERE It_id = ?', [price, item_id]);
                res.status(200).json({
                    message: 'accepted'
                });
            }
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.post('/immediate', async (req, res) => {
        const {body: {item_id, user_id}} = req;
        let conn = null

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT Is_end, Quick_price, U_id'
                                            + ' FROM ITEM'
                                            + ' WHERE It_id = ?', [item_id]);
            const {
                Is_end: is_end,
                Quick_price: quick_price,
                U_id: seller_id
            } = result[0];

            if (is_end !== '0') {
                res.status(400).json({
                    message: '유효하지 않은 상품입니다'
                });
            } else if (user_id === seller_id) {
                res.status(400).json({
                    message: '자신의 상품은 즉시 구매할 수 없습니다'
                });
            } else {
                await conn.query('INSERT INTO BID(Price, U_id, It_id)'
                                + ' VALUES(?, ?, ?)', [quick_price, user_id, item_id]);
                await conn.query('UPDATE ITEM'
                                + ' SET Current_price = Quick_price,'
                                + ` Is_end = '1'`
                                + ' WHERE It_id = ?', [item_id]);
                await conn.query('INSERT INTO ROOM(It_id, Buy_id, Sell_id)'
                                + ' VALUES(?, ?, (SELECT U_id FROM ITEM'
                                                + ' WHERE It_id = ?))', [item_id, user_id, item_id]);
                res.status(200).json({
                    message: 'accepted'
                });
            }
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.post('/register', upload.single('img_file'), async (req, res) => {
        const {
            body: {
                user_id,
                item_name,
                category_id,
                start_price,
                min_bid_unit,
                immediate_price,
                description,
                address,
                expired_date,
                }
            } = req;
        const {file: {path}} = req;
        const img_url = process.env.BASE_URL + path.replace('\\', '/');
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            await conn.query('INSERT INTO ITEM (Name, Description, Min_bid_unit, Quick_price,'
                            + ' Current_price, Expire_Date, Start_price, Img, C_id, U_id, Ad_id)'
                            + ' VALUES(?, ?, ?, ?, ?, ? ,? ,?, ?, ?, (SELECT Ad_id FROM ADDRESS'
                                                                    + ' WHERE Name = ?))',
                                [item_name, description, min_bid_unit, immediate_price,
                                start_price, expired_date, start_price, img_url, category_id,
                                user_id, address]);
            res.status(200).json({
                message: 'accepted'
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    return router;
};