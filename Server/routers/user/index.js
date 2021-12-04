module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();

    router.post('/signin', async (req, res) => {
        const {body: {id, password}} = req;
        let conn = null;
        
        try {
            conn = await pool.getConnection(async conn => conn);
            const [result1] = await conn.query('SELECT * FROM MEMBER'
                                                + ' WHERE U_id = ?'
                                                + ' AND Pw = ?', [id, password]);
            if (!result1.length) {
                res.status(400).json({
                    message: '로그인 정보가 틀립니다'
                });
            } else {
                const user_info = result1[0];
                const [result2] = await conn.query('SELECT A.Name'
                                                    + ' FROM MEMBER M, LIVES_IN L, ADDRESS A'
                                                    + ' WHERE M.U_id = L.U_id'
                                                    + ' AND L.Ad_id = A.Ad_id'
                                                    + ' AND M.U_id = ?', [user_info.U_id]);

                const address_list = result2.map((address) => {
                    return address.Name;
                });

                res.status(200).json({
                    id: user_info.U_id,
                    email: user_info.Email,
                    name: user_info.Name,
                    description: user_info.Description,
                    address: address_list,
                    tel: user_info.Tel
                });
            }
        } catch(err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.post('/signup', async (req, res) => {
        const {body: {id, password, email, name, description, address, tel}} =req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT * FROM MEMBER'
                                                + ' WHERE U_id = ?', [id]);
            
            if (result.length) {
                res.status(400).json({
                    message: '아이디가 중복되었습니다'
                })
            } else {
                await conn.query('INSERT INTO MEMBER'
                                    + ' VALUES(?, ?, ?, ?, ?, ?)',
                                    [id, password, name, description, tel, email]);
                
                for (ad_id of address) {
                    await conn.query('INSERT INTO LIVES_IN'
                    + ' VALUES(?, ?)', [id, ad_id]);
                }
            
                res.status(200).json({
                    id,
                    email,
                    name,
                    description,
                    address,
                    tel
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

    return router;
};