function getRequiredComponents()
  return {walls = {"hitbox", "position", "size"},
    entities = {"hitbox", "speed", "size"}}
end

function update()

  -- Moving walls
  for i, wall in ipairs(entities) do
    for j, entity in ipairs(entities) do
      if wall.hitbox.type == "WALL" and entity.hitbox.type == "WALLREACTOR" then
        local y2 = wall.position.y
        local x2 = wall.position.x
        local w2 = wall.size.width
        local h2 = wall.size.height

        local x = entity.position.x
        local y = entity.position.y
        local w = entity.size.width
        local h = entity.size.height


        -- left/right
        if y + h > y2 + math.abs(wall.speed.dy) and y < y2 + h2 - math.abs(wall.speed.dy) then
          -- left half
          if x + w + entity.speed.dx > x2 + wall.speed.dx and x + w < x2 + w2 / 2 then
            entity.position.x = x2 - w - math.abs(wall.speed.dx) - entity.speed.dx
            entity.speed.dx = wall.speed.dx
          end
          -- right half
          if x + entity.speed.dx < x2 + w2 + wall.speed.dx and x > x2 + w2 / 2 then
            entity.position.x = x2 + w2 + math.abs(wall.speed.dx) - entity.speed.dx
            entity.speed.dx = wall.speed.dx
          end
        end

        -- up/down
        if x + w > x2 + math.abs(wall.speed.dx) and x < x2 + w2 - math.abs(wall.speed.dx) then
          -- top half
          if y + h + entity.speed.dy > y2 + wall.speed.dy and y + h < y2 + h2 / 2 then
            entity.position.y = y2 - h - math.abs(wall.speed.dy) - entity.speed.dy
            entity.speed.dy = wall.speed.dy
          end
          -- bottom half
          if y + entity.speed.dy < y2 + h2 + wall.speed.dy and y > y2 + h2 / 2 then
            entity.position.y = y2 + h2 + math.abs(wall.speed.dy) - entity.speed.dy
            entity.speed.dy = wall.speed.dy
          end
        end
      end
    end
  end

  -- static walls
  for i, wall in ipairs(walls) do
    for j, entity in ipairs(entities) do
      if wall.hitbox.type == "WALL" and entity.hitbox.type == "WALLREACTOR" then
        local y2 = wall.position.y
        local x2 = wall.position.x
        local w2 = wall.size.width
        local h2 = wall.size.height

        local x = entity.position.x
        local y = entity.position.y
        local w = entity.size.width
        local h = entity.size.height

        -- left/right
        if y + h > y2 and y < y2 + h2 then
          -- left half
          if x + w + entity.speed.dx > x2 and x + w < x2 + w2 / 2 then
            entity.position.x = x2 - w
            entity.speed.dx = 0
          end
          -- right half
          if x + entity.speed.dx < x2 + w2 and x > x2 + w2 / 2 then
            entity.position.x = x2 + w2
            entity.speed.dx = 0
          end
        end

        -- up/down
        if x + w > x2 and x < x2 + w2 then
          -- top half
          if y + h + entity.speed.dy > y2 and y + h < y2 + h2 / 2 then
            entity.position.y = y2 - h
            entity.speed.dy = 0
          end
          -- bottom half
          if y + entity.speed.dy < y2 + h2 and y > y2 + h2 / 2 then
            entity.position.y = y2 + h2
            entity.speed.dy = 0
          end
        end
      end
    end
  end
end
